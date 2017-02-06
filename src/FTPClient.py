'''
Created on Feb 6, 2017

@author: coal175
'''

import sys
import socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serverAddress = ('localhost', 10000)
print('connecting to server at addr: %s and port: %s' % serverAddress)
sock.connect(serverAddress)

try:
    
    # send some data to the server
    x = ''
    while x != 'quit':
        x = raw_input('> ')
        
        stringToSend = x
        print('sending message to server')
        sock.sendall(stringToSend)
    
        # response is received on the server side in chunks of 16 
        # as that is the amount set in the param of 'conn.recv'
        # and therefore we need to keep receiving until all the data has been
        # received back here
        # Look for the response
        amount_received = 0
        amount_expected = len(stringToSend)
        
        while amount_received < amount_expected:
            data = sock.recv(16)
            amount_received += len(data)
            print('received "%s"' % data)
finally:
    print('closing connection to server')
    sock.close()