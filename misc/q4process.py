# -*- coding: utf-8 -*-
#!/usr/bin/python
import json
import re
writefile = open('q4_processed','a')
for i in range(15):
        if i < 10:
                filename = 'part-0000'+str(i)
        else :
                filename = 'part-000'+str(i)

        readfile = open(filename,'r')
        for line in readfile:

                start = line.find('{')

                json_t = json.loads(line[start:-1])

                hashtag = line.split(' ')[1]

                text = json_t['text'].replace(",","\u908d\u87f6\u50ea")
                text = re.sub(r"\\",r"\\\\")
                text = text.encode('utf-8')

                writecontent = hashtag+','+text

                writefile.write(writecontent)
                writefile.write('\n')
        print filename + ' completed'




