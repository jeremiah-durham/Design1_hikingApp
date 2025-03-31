import csv

def generate_inserts(parks, dps):
    # generate parks values
    p = 'INSERT INTO parks (id, name)\nVALUES\n'
    for (id, park) in zip(range(1, len(parks)+1),parks):
        p += f'\t({id}, "{park}"){',' if id < len(parks) else ';'}\n'
    trl = 'INSERT INTO trails (park_id, name, distance, elevation_delta, difficulty, est_time_hr, est_time_min)\nVALUES\n'
    trt = 'INSERT INTO traits (trail_id, hiking, biking, mountain_views, river, forest, hist_sites, lake)\nVALUES\n'
    for (id, dp) in zip(range(1, len(dps)+1), dps):
        trl += f'\t({dp['park_id']}, "{dp['trail_name']}", {dp['distance']}, {dp['elevation_delta']}, "{str.lower(dp['difficulty'])}", {dp['est_time_hr']}, {dp['est_time_min']}){',' if id < len(dps) else ';'}\n'
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

    with open('data.sql', 'w') as f:
        f.write('USE project;\n\n')
        for s in generate_inserts(parks, datapoints):
            f.write(s)

