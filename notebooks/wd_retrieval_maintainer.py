#!/usr/bin/env python
# coding: utf-8

# # Wikidata Retrieval Maintainer

# ## Requirements
# 
# 1. Folder structure and files from github repo
#    -> clone repo
# 2. Cut identifier list 
#    -> cd notebooks
#    -> python wd_setList.py <your_name>      (i.e. tillman or kevin)
#    -> python wd_retrieval_maintainer.py
# 3. Check manually whether Data/wikidata/wd_species_df.csv is updated
#    (important, because that is the file where all results are intermediately saved)


from datetime import datetime, timedelta
import time
from wd_species import *



# ## Retrieve Results

start_time = datetime.now()
print('\n********\n Retrieval Start at: ', start_time.strftime("%D - %H:%M:%S"), '\n')

while getCurrentTime() < start_time + timedelta(days=7):
    
    intermediate_end_time = getCurrentTime() + timedelta(hours=1)
    
    # retrieve results
    retrievalRestarter(intermediate_end_time)

print('\n\n Retrieval process ended. \nSeven days retrieval goal reached.')
    
# ## Convert Results from DataFrame to XML

print('\n\n Create XML file')
createXML()

