#!/usr/bin/python
#
# Requires:
#   https://github.com/liris/websocket-client - install from source released version is buggy
#   http://docs.python-requests.org/en/latest/
#   http://stickpeople.com/projects/python/win-psycopg/
#

import websocket
import requests
import thread
import time
import sys
import os
import json
import psycopg2   
import md5
from time import gmtime, strftime

def on_message(ws, message):
    print 'message ('+ strftime("%Y-%m-%d %H:%M:%S", gmtime()) +'):'
    print json.dumps(json.loads(message), sort_keys=True, indent=4)

def on_error(ws, error):
    print 'error:'
    print error

def on_close(ws):
    print "### closed ###"

def on_open(ws):
    print '### opened ###'

if __name__ == "__main__":
    params = {'host':'localhost','database':'cahoots','user':'postgres','password':'admin'}
    connection = psycopg2.connect(**params)
    cur = connection.cursor()
    username = str(os.getpid())
    password = str(os.getpid())
    
    m = md5.new()
    m.update(password)
    cur.execute(
    "INSERT INTO Users (username, name, password, role) VALUES (%s, %s, %s, %s);", (username,username,m.hexdigest(),2))
    connection.commit()
    authToken = requests.post(
        "http://localhost:9000/app/login", data={
            'username': username,
            'password': password
        }).text
    cur.execute("DELETE FROM Users WHERE username = %s;", (username,))
    connection.commit()
    cur.close()
    connection.close()
    print "Logged in as {0}:{1}".format(os.getpid(), authToken)
    websocket.enableTrace(False)
    ws = websocket.WebSocketApp('ws://localhost:9000/app/message?auth_token=' + authToken,
            on_message = on_message,
            on_error = on_error,
            on_close = on_close)
    ws.on_open = on_open
    ws.run_forever()
        