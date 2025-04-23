import os
import decimal
from flask import Flask, request, json
import mysql.connector
from abc import ABC, abstractmethod
from apscheduler.schedulers.background import BackgroundScheduler
import atexit

QUERY_TAGS = {'fields', 'filters', 'count'}


class QueryFactory:
    def __init__(self):
        self.select = None
        self.filters = None
        self.count = None

    def withFields(self, fields: list):
        self.select = fields

    def withFilters(self, filters: dict):
        self.filters = filters

    def withCount(self, count):
        self.count = count

    def __generateSelect(self):
        s = 'SELECT '
        s += ', '.join(self.select) if self.select and len(self.select) > 0 else '*'
        s += '\nFROM trails\n'
        s += '\tINNER JOIN parks ON trails.park_id = parks.id\n'
        s += '\tINNER JOIN traits ON trails.id = traits.trail_id\n'
        return s

    def __generateFilters(self):
        if not self.filters:
            return ''
        filts = []
        for key in self.filters.keys():
            if key in ['park_name', 'difficulty']:
                filts.append(QFilterDisj(key, self.filters[key]))
            elif key in ['elevation_delta', 'distance', 'est_time_min']:
                filts.append(QFilterRange(key, self.filters[key]))
            elif key == 'traits':
                filts.append(QFilterConjMany(self.filters[key]))
            else:
                raise Exception('Invalid filter key: ' + key)
        sqlfilter = "WHERE\n"
        sqlfilter = sqlfilter + \
            ' AND\n'.join(list(map(lambda x: '\t' + str(x), filts))) + '\n'
        return sqlfilter

    def __generateCount(self):
        if not self.count:
            return ''
        return 'LIMIT ' + str(self.count) + '\n'

    def build(self):
        s = ''
        s += self.__generateSelect()
        s += self.__generateFilters()
        s += self.__generateCount()
        return s


class QFilterGeneric(ABC):
    @abstractmethod
    def __str__(self):
        # this should return the sql selection string for this class
        pass


class QFilterDisj(QFilterGeneric):
    def __init__(self, field, filters: list):
        self.field = field
        self.filters = filters

    def _getConditions(self):
        # this should return a list of strings which are the sql conditions
        conditions = [f'{self.field} = \"{f}\"' for f in self.filters]
        return conditions

    def __str__(self):
        return '(' + ' OR '.join(self._getConditions()) + ')'


class QFilterConj(QFilterGeneric):
    @abstractmethod
    def _getConditions(self):
        # this should return a list of strings which are the sql conditions
        pass

    def __str__(self):
        return '(' + ' AND '.join(self._getConditions()) + ')'


class QFilterConjMany(QFilterConj):
    def __init__(self, filters: dict):
        self.filters = filters

    def _getConditions(self):
        conditions = [f'{k} = {v}' for (k, v) in self.filters.items()]
        return conditions


class QFilterRange(QFilterConj):
    def __init__(self, field, filters: dict):
        self.field = field
        self.upper = filters['leq'] if 'leq' in filters.keys() else None
        self.lower = filters['geq'] if 'geq' in filters.keys() else None

    def _getConditions(self):
        # should be self.field = self.upper, self.field = self.lower
        conditions = []
        if self.upper:
            conditions.append(f'{self.field} <= {self.upper}')
        if self.lower:
            conditions.append(f'{self.field} >= {self.lower}')
        return conditions


class DBManager:
    def __init__(self, database='example', host="db", user="root", password_file=None):
        pf = open(password_file, 'r')
        self.connection = mysql.connector.connect(
            user=user,
            password=pf.read(),
            host=host,
            database=database,
            port=3306
        )

        pf.close()

        self.cursor = self.connection.cursor()

    def query_titles(self):
        self.cursor.execute('SELECT title FROM blog')
        rec = []
        for c in self.cursor:
            rec.append(c[0])
        return rec

    def query_trails(self, cols=['trail_name', 'park_name']):
        self.cursor.execute(f'SELECT {", ".join(cols)} FROM trails INNER JOIN parks ON trails.park_id = parks.id INNER JOIN traits ON trails.id = traits.trail_id')
        res = []
        for c in self.cursor:
            res.append(c)
        return json.jsonify(list(map(lambda x: {k: v for (k, v) in zip(cols, x)}, res)))

    def custom_query(self, query, cols):
        self.cursor.execute(query)
        res = []
        for c in self.cursor:
            res.append(c)
        return json.jsonify(list(map(lambda x: {k: v for (k, v) in zip(cols, x)}, res)))

    def create_user(self, user: dict):
        insertstr = 'INSERT INTO users(uuid, name, eemail, weight, height) VALUES ('
        insertstr += '@uuid := UUID_TO_BIN(UUID()), '
        insertstr += ' ,'.join([f'"{user["name"]}"', f'"{user["eemail"]}"',
                               str(user['weight']), str(user['height'])])
        insertstr += ' )'
        self.cursor.execute(insertstr)
        self.connection.commit()
        self.cursor.execute('SELECT @uuid')
        res = ''
        for c in self.cursor:
            res = c[0]
        return res.hex()

    def query_columns(self):
        res = set()
        for t in ['trails', 'traits', 'parks']:
            self.cursor.execute(f'SHOW COLUMNS FROM {t}')
            for c in self.cursor:
                res.update([c[0]])
        return res


class JsonEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, decimal.Decimal):
            return float(obj)
        return json.JSONEncoder.default(self, obj)


server = Flask(__name__)
server.json_encoder = JsonEncoder
conn = None
scheduler = BackgroundScheduler(daemon=True)


@server.route('/')
def getTrails():
    global conn
    if not conn:
        conn = DBManager(
            database='project',
            password_file='/run/secrets/db-password'
        )

    if 'fields' in request.args.keys():
        res = conn.query_trails(cols=request.args.get('fields', '').split(','))
    else:
        res = json.jsonify(list(conn.query_columns()))
    return res


@server.route('/api')
def api_route():
    return request.args


@server.post('/json')
def json_test():
    global conn
    if not conn:
        conn = DBManager(
            database='project',
            password_file='/run/secrets/db-password'
        )
    if not request.is_json:
        return "<div> Please post a JSON request </div>"
    else:
        data = None
        try:
            data = request.get_json()
        except Exception as e:
            return json.jsonify({"message": "got bad data", "error": str(e)})
        if 'fields' not in data.keys():
            return {"message": "'fields' entry must be present"}
        if type(data['fields']) != list:
            return {"message": "'fields' entry must be a list"}
        valid_columns = conn.query_columns()
        fields = set(data['fields'])
        if len(fields - valid_columns) > 0:
            return {"message": "'fields' contains invalid entries", "invalid-fields": f'{fields - valid_columns}'}
        if 'count' in data.keys() and type(data['count']) != int:
            return {"message": "'count' entry must be an int"}

        fac = QueryFactory()
        if 'fields' in data.keys():
            fac.withFields(data['fields'])
        if 'filters' in data.keys():
            fac.withFilters(data['filters'])
        if 'count' in data.keys():
            fac.withCount(data['count'])

        query = fac.build()
        out = conn.custom_query(query, data['fields'])

        return out


@server.post('/user')
def create_user():
    global conn
    if not conn:
        conn = DBManager(
            database='project',
            password_file='/run/secrets/db-password'
        )
    if not request.is_json:
        return "<div> Please post a JSON request </div>"
    else:
        data = None
        try:
            data = request.get_json()
        except Exception as e:
            return json.jsonify({"message": "got bad data", "error": str(e)})
        if set(data.keys()) != {'name', 'weight', 'height', 'eemail'}:
            return {"message": f"request has invalid entries: f{set(data.keys()) - {'name', 'weight', 'height', 'eemail'}}"}

        # do user creation process
        uuid = conn.create_user(data)
        server.logger.debug(f"Created user with uuid {uuid}")
        return {'uuid': uuid}


def my_job():
    if os.environ.get('WERKZEUG_RUN_MAIN'):
        return
    server.logger.warn("Task ran...")
    server.logger.warn(os.environ.get('WERKZEUG_RUN_MAIN'))
    # server.logger.debug(str(dir(server)))


scheduler.add_job(func=my_job, id="test", trigger='interval', seconds=10)
scheduler.start()

if __name__ == '__main__':
    server.run(debug=False)
    atexit.register(lambda: scheduler.shutdown())
