import requests


def test_query():
    query = {
        'fields':['trail_name','park_name','difficulty','distance'],
        'filters':{
            'difficulty':['easy', 'moderate'],
            'traits':{
                    'river':True
            },
        },
        'count':5
    }

    r = requests.post('http://localhost:80/json', json=query)
    print(r.status_code)
    print(r.json()['data'])

if __name__ == '__main__':
    r = requests.get('http://localhost:80')
    print(r.status_code)
    print(r.headers['content-type'])
    print(r.text)
    print(r.json())


    print('\n'*5)
    test_query()
