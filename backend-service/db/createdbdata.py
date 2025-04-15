import csv

import requests
import json

def getLatAndLon(trailname:str):
    url = "https://www.alltrails.com/widget/trail/us/colorado/"+trailname.replace(' ', '-')+"?elevationDiagram=false"
    x = requests.get(url)
    if x.status_code != 200:
        return ("null","null")

    t = x.text
    t = t.split("window.MAP_WIDGET_PROPS = ")
    t = t[1].split('</script>')[0]
    d = json.loads(t)
    lat = d['trail']['location']['latitude']
    lon = d['trail']['location']['longitude']

    return (str(lat),str(lon))

def generate_inserts(parks, dps):
    # generate parks values
    p = 'INSERT INTO parks (id, park_name)\nVALUES\n'
    for (id, park) in zip(range(1, len(parks)+1),parks):
        p += f'\t({id}, "{park}"){',' if id < len(parks) else ';'}\n'
    trl = 'INSERT INTO trails (park_id, trail_name, distance, elevation_delta, difficulty, est_time_min, lat, lon)\nVALUES\n'
    trt = 'INSERT INTO traits (trail_id, hiking, biking, mountain_views, river, forest, hist_sites, lake)\nVALUES\n'
    for (id, dp) in zip(range(1, len(dps)+1), dps):
        trl += f'\t({dp['park_id']}, "{dp['trail_name']}", {dp['distance']}, {dp['elevation_delta']}, "{str.lower(dp['difficulty'])}", {dp['est_time_min']}, {",".join(getLatAndLon(dp['trail_name']))}){',' if id < len(dps) else ';'}\n'
        trt += f'\t({id}, {'true' if dp['hiking'] == 'Y' else 'false'}, {'true' if dp['biking'] == 'Y' else 'false'}, {'true' if dp['mountain_views'] == 'Y' else 'false'}, {'true' if dp['river'] == 'Y' else 'false'}, {'true' if dp['forest'] == 'Y' else 'false'}, {'true' if dp['hist_sites'] == 'Y' else 'false'}, {'true' if dp['lake'] == 'Y' else 'false'}){',' if id < len(dps) else ';'}\n'


    return (p, trl, trt)


    

if __name__ == '__main__':
    cols = []

    datapoints = []

    with open('trailinfo.csv') as csvfile:
        rdr = csv.reader(csvfile, delimiter=',')


        cols = list(next(rdr))
        for row in rdr:
            dp = dict()
            for s in zip(cols, row):
                dp[s[0]]=s[1]
            datapoints.append(dp)

    parks = list({x['park_name'] for x in datapoints})

    cols.append('park_id')
    for dp in datapoints:
        dp['park_id'] = parks.index(dp['park_name']) + 1

    # write 'null' to any entries that are empty so the correct code is generated
    for dp in datapoints:
        for c in cols:
            if dp[c] == '':
                dp[c] = 'null'
        if not (dp['est_time_min'] == 'null' and dp['est_time_hr'] == 'null'):
            if dp['est_time_min'] == 'null':
                dp['est_time_min'] = float(dp['est_time_hr'])*60
            else:
                dp['est_time_min'] = float(dp['est_time_min']) + (float(dp['est_time_hr'])*60 if dp['est_time_hr'] != 'null' else 0)

    with open('trail_data.sql', 'w') as f:
        f.write('USE project;\n\n')
        for s in generate_inserts(parks, datapoints):
            f.write(s)

