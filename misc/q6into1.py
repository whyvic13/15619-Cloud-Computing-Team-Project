# -*- coding: utf-8 -*-
#!/usr/bin/python
import re
import json
import time
import datetime
import calendar

fwrite = open('q6final','w')

for i in range(10):
	fileName = 'part-0000' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)
			
			fwrite.write(str(a['tweet_id']) + ',')
			tmp = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\r","\\r").replace("\n","\\n")

			fwrite.write(tmp.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass


for i in range(10):
	fileName = 'part-0001' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)
			
			fwrite.write(str(a['tweet_id']) + ',')
			tmp = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\r","\\r").replace("\n","\\n")

			fwrite.write(tmp.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass

for i in range(10):
	fileName = 'part-0002' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)
			
			fwrite.write(str(a['tweet_id']) + ',')
			tmp = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\r","\\r").replace("\n","\\n")

			fwrite.write(tmp.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass

for i in range(9):
	fileName = 'part-0003' + str(i)
	fread = open(fileName)	
	for line in fread:
		try:
			a = json.loads(line)
			
			fwrite.write(str(a['tweet_id']) + ',')
			tmp = a['text'].replace(",","\\u908d\\u87f6\\u50ea").replace("\\","\\\\").replace("\r","\\r").replace("\n","\\n")

			fwrite.write(tmp.encode('utf-8'))
			fwrite.write('\n')
		except:
			pass

