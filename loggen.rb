require 'socket'
require 'securerandom'
require 'time'

puts 'Waiting...'

server= TCPServer.open(12345)
sock = server.accept

puts 'Accept'

while true do
	data = []
	data <<= Time.now.to_i
	data << SecureRandom.uuid
	data << rand(1...10000)
	data << rand(100)
	str = data.join(',') + "\n"

	sock.write(str)
	sock.write(str) if rand(10000) == 1

	sleep(0.01)
end

sock.close()
server.close()

