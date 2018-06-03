from __future__ import print_function
import sys
from random import random
from random import uniform

count_linee = 0

for line in sys.stdin:
    count_linee += 1
    rndNum = uniform(0,1)
    print("rndNum "+str(rndNum)+ "; count_linee: "+str(count_linee))
    if rndNum < 0.2:
        print("do pass..")
        pass

print(count_linee)
