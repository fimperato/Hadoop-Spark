from random import random
import sys

if random()<0.05:
    raise SystemExit("Sono fortunato!")

for line in sys.stdin:
    if random() < 0.5:
        raise SystemExit("Sono fortunato (interno al for)!")
    pass
