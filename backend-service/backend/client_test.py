import requests
import asyncio


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
    print(r.json())


async def test_user():
    print("Testing user creation")
    user_info = {
        "name": "John Doe",
        "eemail": "validemail@mail.co",
        "weight": 120,
        "height": 67
    }

    r = requests.post("http://localhost:80/user", json=user_info)
    uuid = r.json()['uuid']
    print(f"Got uuid: {uuid}")

    # wait a bit before more testing
    await asyncio.sleep(5)

    # try starting a hike
    hike_event = {
        "user_uuid": uuid,
        "trail_id": 2,
        "action": "START"
    }
    print("Starting hike...")
    r = requests.post("http://localhost:80/hikes", json=hike_event)
    print(f"Request status: {r.status_code}")
    if r.headers['content-type'] == "application/json":
        print(f"Got json response: {r.json()}")
    else:
        print(f"Got non-json response: {r.text}")

    # try to start a hike with one already started
    print("Starting hike again...")
    hike_event['trail_id'] = 6
    r = requests.post("http://localhost:80/hikes", json=hike_event)
    print(f"Request status: {r.status_code}")
    if r.headers['content-type'] == "application/json":
        print(f"Got json response: {r.json()}")
    else:
        print(f"Got non-json response: {r.text}")

    print("Stopping hike...")
    hike_event['trail_id'] = 5
    hike_event['action'] = "STOP"
    r = requests.post("http://localhost:80/hikes", json=hike_event)
    print(f"Request status: {r.status_code}")
    if r.headers['content-type'] == "application/json":
        print(f"Got json response: {r.json()}")
    else:
        print(f"Got non-json response: {r.text}")

    print("Stopping non-started hike...")
    hike_event['trail_id'] = 5
    hike_event['action'] = "STOP"
    r = requests.post("http://localhost:80/hikes", json=hike_event)
    print(f"Request status: {r.status_code}")
    if r.headers['content-type'] == "application/json":
        print(f"Got json response: {r.json()}")
    else:
        print(f"Got non-json response: {r.text}")


if __name__ == '__main__':
    r = requests.get('http://localhost:80')
    print(r.status_code)
    print(r.headers['content-type'])
    print(r.text)
    print(r.json())


    print('\n'*5)
    test_query()
    asyncio.run(test_user())
