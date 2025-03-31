import os
from flask import Flask, request
import mysql.connector

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

    def populate_db(self):
        self.cursor.execute('DROP TABLE IF EXISTS blog')
        self.cursor.execute('CREATE TABLE blog (id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255))')
        self.cursor.executemany('INSERT INTO blog (id, title) VALUES (%s, %s);', [(i, 'Blog post #%d' % i) for i in range(1, 5)])
        self.connection.commit()

    def query_titles(self):
        self.cursor.execute('SELECT title FROM blog')
        rec = []
        for c in self.cursor:
            rec.append(c[0])
        return rec


server = Flask(__name__)
conn = None

@server.route('/')
def listBlog():
    global conn
    if not conn:
        conn = DBManager(
                database='project',
                password_file='/run/secrets/db-password'
        )
        conn.populate_db()
    rec = conn.query_titles()

    response = ''
    for c in rec:
        response = response + '<div>  Hello ' + c + '</div>'
    return response

@server.route('/api')
def api_route():
    return request.args

if __name__ == '__main__':
    server.run()
