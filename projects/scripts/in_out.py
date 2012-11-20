#!/usr/bin/python
#
# Requires:
#   https://github.com/liris/websocket-client - install from source released version is buggy
#   http://docs.python-requests.org/en/latest/
#

import requests
import os
import time
from websocket import create_connection

if __name__ == "__main__":
    while True:
        authToken = requests.post(
            "http://localhost:9000/app/login", data={
                'username': str(os.getpid()),
                'password': str(os.getpid())
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
        

