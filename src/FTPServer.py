'''
Created on Feb 6, 2017

@author: coal175
'''
import sys
import socket

# create the socket to listen for the connection
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# set the port number of the socket that we created
server_address = ('localhost', 10000)
print >>sys.stderr, 'starting up on %s port %s' % server_address
sock.bind(server_address)

# begin listening for incoming ocnnections
sock.listen(1)

while True:
    # waiting... once a conenction is found, socket.accept is hit
    # and will return connetion and the addr of client
    print >>sys.stderr, 'waiting for a connection'
    conn, clientAddr = sock.accept()
    
    try:
        print >>sys.stderr, 'connection made from address ', clientAddr
        
        # at this point we have a connection made as well as the addr and connection
        # of the client connected.  We can receive what they have sent through
        # conn.recv(bytes) and then send information back though the command
        # conn.sendall(bytes)
        while True:
            data = conn.recv(16)
            print >>sys.stderr, 'received "%s"' % data
            if data:
                # there is data received and we want to send something back
                print >>sys.stderr, 'sending "%s" back to client' % data
                conn.sendall(data)
            else:
                #no more data from client
                print >>sys.stderr, 'no more data from', clientAddr
                break
            
    
    finally:
        # finish things up by closing the connetion and cleaning up
        conn.close()