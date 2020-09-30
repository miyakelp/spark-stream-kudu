require 'socket'

puts 'Waiting...'

server= TCPServer.open(12345)
sock = server.accept

puts 'Accept'

while true do
	sock.write("Yo\n")
	sleep(0.5)
end

sock.close()
server.close()

