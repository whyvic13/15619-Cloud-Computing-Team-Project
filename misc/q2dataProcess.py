# -*- coding: utf-8 -*-
#!/usr/bin/python
import re
import json
import time
import datetime
import calendar

fwrite = open('q2final','w')


for i in range(10):
	fileName = 'part-0000' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)

			time = calendar.timegm(datetime.datetime.strptime(a['created_at'], "%a %b %d %H:%M:%S +0000 %Y").timetuple())
			fwrite.write(str(a['user_id']) +','
				+ str(time) + ',' 
				+ str(a['tweet_id']) + ':' 
				+ str(a['score']) + ':')
			text = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r")

			fwrite.write(text.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass

for i in range(10):
	fileName = 'part-0001' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)

			time = calendar.timegm(datetime.datetime.strptime(a['created_at'], "%a %b %d %H:%M:%S +0000 %Y").timetuple())
			fwrite.write(str(a['user_id']) +','
				+ str(time) + ',' 
				+ str(a['tweet_id']) + ':' 
				+ str(a['score']) + ':')
			text = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r")

			fwrite.write(text.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass

for i in range(10):
	fileName = 'part-0002' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)

			time = calendar.timegm(datetime.datetime.strptime(a['created_at'], "%a %b %d %H:%M:%S +0000 %Y").timetuple())
			fwrite.write(str(a['user_id']) +','
				+ str(time) + ',' 
				+ str(a['tweet_id']) + ':' 
				+ str(a['score']) + ':')
			text = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r")

			fwrite.write(text.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass

for i in range(10):
	fileName = 'part-0003' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)

			time = calendar.timegm(datetime.datetime.strptime(a['created_at'], "%a %b %d %H:%M:%S +0000 %Y").timetuple())
			fwrite.write(str(a['user_id']) +','
				+ str(time) + ',' 
				+ str(a['tweet_id']) + ':' 
				+ str(a['score']) + ':')
			text = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r")

			fwrite.write(text.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass

for i in range(10):
	fileName = 'part-0004' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)

			time = calendar.timegm(datetime.datetime.strptime(a['created_at'], "%a %b %d %H:%M:%S +0000 %Y").timetuple())
			fwrite.write(str(a['user_id']) +','
				+ str(time) + ',' 
				+ str(a['tweet_id']) + ':' 
				+ str(a['score']) + ':')
			text = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r")

			fwrite.write(text.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass

for i in range(10):
	fileName = 'part-0005' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)

			time = calendar.timegm(datetime.datetime.strptime(a['created_at'], "%a %b %d %H:%M:%S +0000 %Y").timetuple())
			fwrite.write(str(a['user_id']) +','
				+ str(time) + ',' 
				+ str(a['tweet_id']) + ':' 
				+ str(a['score']) + ':')
			text = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r")

			fwrite.write(text.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass

for i in range(10):
	fileName = 'part-0006' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)

			time = calendar.timegm(datetime.datetime.strptime(a['created_at'], "%a %b %d %H:%M:%S +0000 %Y").timetuple())
			fwrite.write(str(a['user_id']) +','
				+ str(time) + ',' 
				+ str(a['tweet_id']) + ':' 
				+ str(a['score']) + ':')
			text = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r")

			fwrite.write(text.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass

for i in range(9):
	fileName = 'part-0007' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)

			time = calendar.timegm(datetime.datetime.strptime(a['created_at'], "%a %b %d %H:%M:%S +0000 %Y").timetuple())
			fwrite.write(str(a['user_id']) +','
				+ str(time) + ',' 
				+ str(a['tweet_id']) + ':' 
				+ str(a['score']) + ':')
			text = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r")

			fwrite.write(text.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass
