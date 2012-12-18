#!/usr/bin/python
#
# Requires:
#   https://github.com/liris/websocket-client - install from source released version is buggy
#   http://docs.python-requests.org/en/latest/
#   http://stickpeople.com/projects/python/win-psycopg/
#

import requests
import md5
import os
import time
import psycopg2   
from websocket import create_connection

if __name__ == "__main__":
    params = {'host':'localhost','database':'cahoots','user':'postgres','password':'admin'}
    connection = psycopg2.connect(**params)
    cur = connection.cursor()
    username = str(os.getpid())
    password = str(os.getpid())
    
    m = md5.new()
    m.update(password)
    try:
        cur.execute(
        "INSERT INTO Users (username, name, password, role) VALUES (%s, %s, %s, %s);", (username,username,m.hexdigest(),2))
        connection.commit()
    
        while True:
            authToken = requests.post(
                "http://localhost:9000/app/login", data={
                    'username': username,
                    'password': password
                }).text
            print "Logged in as {0}:{1}".format(os.getpid(), authToken)
            
            ws = create_connection('ws://localhost:9000/app/message?auth_token=' + authToken)
            time.sleep(5)
            
            requests.post(
            "http://localhost:9000/app/logout", data={
                'auth_token': authToken
            }).text
            
            print "Logged out"
            ws.close()
            time.sleep(5)
    except KeyboardInterrupt:
        print 'bye'
    finally:
        cur.execute("DELETE FROM Users WHERE username = %s;", (username,))
        connection.commit()
        cur.close()
        connection.close()

