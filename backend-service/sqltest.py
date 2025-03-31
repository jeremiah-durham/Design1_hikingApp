import mysql.connector

try:
    mydb = mysql.connector.connect(
            host='localhost',
            port=3306,
            database='project',
            user='root',
            password='foo'
    )
except Exception as err:
    print("An error occured:", err)
    print(type(err))
    connected = False
else:
    connected = mydb.is_connected()

print(f'Server connected: {connected}')

if connected:
    mycursor = mydb.cursor()

    mycursor.execute('DROP TABLE IF EXISTS test')
    res = mycursor.fetchall()
    print(res)

    mycursor.execute('CREATE TABLE test (id INT AUTO_INCREMENT PRIMARY KEY, i INT)')
    res = mycursor.fetchall()
    print(res)

    mycursor.execute('INSERT INTO test (i) VALUES (38), (989), (444)')
    res = mycursor.fetchall()
    print(res)

    mycursor.execute('SELECT * FROM test')
    res = mycursor.fetchall()
    print(res)

    # commit changes to db
    mydb.commit()

    mydb.disconnect()

print("Done")
