#!/usr/bin/env python
# coding: utf-8


import sys
import pandas as pd

person = None

try:
    person = sys.argv[1]
except:
    pass





path_ids = '../data/wikidata/wd_identifier_list_1.csv'
identifier_list = list(pd.read_csv(path_ids)['resources'])

if person == None:
    print(' Execute wd_setList.py again. \n Set tillman or kevin as agument.')

elif person == 'tillman' or person == 'kevin':   
    
    print('Start cutting list.')
    
    if person == 'tillman':
        start = 10000
        end   = 20000 
    elif person == 'kevin':
        start = 20000
        end   = 30000
        
    df_identifier_list = pd.DataFrame(identifier_list[start:end], columns = ['resources'])
    df_identifier_list.to_csv(path_ids,  index = False)
    
    print('Identifier list successfully cutted')

