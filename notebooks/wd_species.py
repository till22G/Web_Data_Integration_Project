#!/usr/bin/env python
# coding: utf-8

# # Retrieval of species data for Wikidata

# In[ ]:


import time
from datetime import datetime, timedelta
import pandas as pd
import xml.etree.ElementTree as ET
from xml.etree.ElementTree import Element, SubElement, Comment, tostring
from SPARQLWrapper import SPARQLWrapper, JSON, XML


# ## Preparation
# ### Namings

# In[ ]:


def getPathIDs():
    path_ids = '../Data/wikidata/wd_identifier_list_1.csv'
    return path_ids

def getPathXML():
    path_XML = '../xml/wd_species.xml'
    return path_XML

def getPathDF():
    path_df = '../Data/wikidata/wd_species_df.csv'
    return path_df


# In[ ]:


def getMappingDict():
    
    mapping_dict = {
       'resource': 'ID', 
       #'family',
       #'class',
       #'order',
       'taxonName': 'Scientific_Name', 
        
       'taxonCommonName': {
           'SubElement': 'Common_Names',
           'SubSubElement': 'Common_Name'
       },
       #'differentFrom',
       #'endemicTo',
       #'conservationStatus',
       'resourceLabel': {
           'SubElement': 'Labels',
           'SubSubElement': 'Label'
       }, 
       'familyLabel': {
           'SubElement': 'Families',
           'SubSubElement': 'Family'
       },
       'classLabel': 'Category',
       'orderLabel': {
           'SubElement': 'Orders',
           'SubSubElement': 'Order'
       },
       'differentFromLabel': {
           'SubElement': 'Different_From_l',
           'SubSubElement': 'Different_From'
       },
       'endemicToLabel': {
           'SubElement': 'Endemic_To_l',
           'SubSubElement': 'Endemic_To'
       },
       'conservationStatusLabel':  {
           'SubElement': 'Listing_Statuses',
           'SubSubElement': 'Listing_Status'
       }
    }
    
    return mapping_dict


# ### Queries

# In[ ]:


def getSpeciesQuery():
    species_query = """

    SELECT ?resource

    WHERE {
      ?resource wdt:P105 wd:Q7432.
    }
    """

    return species_query


# In[ ]:


def getAttributeQuery():
    attribute_query = """

    SELECT DISTINCT
      ?resource
      ?family
      ?class
      ?order
      ?taxonName ?taxonCommonName 
      ?differentFrom ?endemicTo ?conservationStatus


      ?resourceLabel
      ?familyLabel
      ?classLabel
      ?orderLabel
      ?differentFromLabel ?endemicToLabel ?conservationStatusLabel


    WHERE 
    {{

      VALUES ?resource {{<{}>}}

      ## optional variables 

        OPTIONAL {{?resource wdt:P171+ ?family.
                  ?family wdt:P105 wd:Q35409.

               OPTIONAL {{?family wdt:P171+ ?order.
                         ?order wdt:P105 wd:Q36602.

                        OPTIONAL {{?order wdt:P171+ ?class.
                                  ?class wdt:P105 wd:Q37517.}}}}}}

      # Taxon name
      OPTIONAL {{?resource wdt:P225 ?taxonName.
                  #FILTER (langMatches( lang(?taxonName), "en" ) )
                  }}
      # Taxon common name
      OPTIONAL {{?resource wdt:P1843 ?taxonCommonName.
                  FILTER (langMatches( lang(?taxonCommonName), "en" ) )}}
      # Different from 
      OPTIONAL {{?resource wdt:P1889 ?differentFrom.}}
      # endemic to
      OPTIONAL {{?resource wdt:P183 ?endemicTo.}}
      # conservation status 
      OPTIONAL {{?resource wdt:P141 ?conservationStatus.}}

      SERVICE wikibase:label {{ bd:serviceParam wikibase:language "en". }}
    }}

    """
    
    return attribute_query


# In[ ]:


def getTaxonNameQuery():
    taxonName_query = """

    SELECT DISTINCT
      ?resource
      ?taxonName

    WHERE 
    {{

      VALUES ?resource {{{}}}

      # Taxon name
      ?resource wdt:P225 ?taxonName.
      
    }}

    """
    
    return taxonName_query


# ### Function Definitions 

# In[ ]:


def runQuery(query, format):

    sparql = SPARQLWrapper("https://query.wikidata.org/sparql")

    sparql.setQuery(query)
    sparql.setReturnFormat(format) 

    try :
        results = sparql.query().convert()
        return results

    except :
        
        #raise Exception(' - SPARQL Query unsuccessful')
        return 'Error - too many requests'


# In[ ]:


def collectAndSaveIdentifiers():
    
    species_query = getSpeciesQuery()
    path_ids = getPathIDs
    
    # run sparql query
    species_results = runQuery(species_query, JSON)
    
    # retrieve identifier
    identifier_list = []
    for identifier in species_results['results']['bindings']:
        identifier_list.append(identifier['resource']['value'])

    # write down list as csv file
    df_identifier_list = pd.DataFrame(identifier_list, columns = ['resources'])
    df_identifier_list.to_csv(path_ids,  index = False)
        
    return identifier_list


# In[ ]:


def createDataframe(path_df = getPathDF()):
    # test whether result collection dataframe already defined, 
    # else retrieve columns from query and create dataframe 
    
    #path_df = getPathDF()
    
    try: 
        df_results = pd.read_csv(path_df)

    except:
        attribute_query = getAttributeQuery()
        
        identifier_list = getIdentifierList()
        
        results = runQuery(attribute_query.format(identifier_list[0]), JSON)
        columns = results['head']['vars']
        df_results = pd.DataFrame(columns = columns)

    return df_results


# In[ ]:


# convert strings of lists to list of stings in df
def convertStrToList(s):
    
    result = s
    if type(s) == str:
        if s[0] == '[' and s[-1] == ']':

            s = s.replace('\"', '')
            s = s.replace('\'', '')
            
            l_s = s[1:-1].split(', ')
            
            result = l_s
            
    return result


# In[ ]:


def convertResultsToDict(results):

    # create new results dict to collect and combine attributes
    r_dict = {}

    # loop through different result sets for this resource
    for result_set in results['results']['bindings']:


        # loop through all attributes for this result set
        for attribute in result_set:
            #print(attribute, result_set[attribute]['value'])

            # assign new value (in lower case)
            new_value = result_set[attribute]['value'].lower()

            # if attribute not yet seen, add to dict
            if attribute not in r_dict.keys():
                # assign string to attribute 
                r_dict[attribute] = new_value

            # if attribute already in dict
            else:
                old_value = r_dict[attribute]

                # if value is single string, create list of old and new value and assign back
                if type(old_value) == str and old_value != new_value:
                    l = []
                    l.append(old_value)
                    l.append(new_value)
                    r_dict[attribute] = l

                # if value is list, append new_value
                elif type(old_value) == list and new_value not in old_value:
                    old_value.append(new_value)

                #else:
                #    raise Exception('Merging of results in JSON: Formating of dictionary values wrong')

    return r_dict
    


# In[ ]:


def appendDataframe(r_dict, df_results, writeOut = True):
    
    path_df = getPathDF()
    
    # append new entry to backup dataframe            
    df_new_entry = pd.DataFrame.from_dict([r_dict])
    df_results = pd.concat([df_results, df_new_entry])

    # save dataframe
    if writeOut: 
        df_results.to_csv(path_df, index=False)

    return df_results


# In[ ]:


def getCurrentTime():
    return datetime.now()


# In[ ]:


def appendXML(root, r_dict, save_each_entry=False):
    
    mapping_dict = getMappingDict()

    # for each new resource, create new species
    new_element = SubElement(root, 'Species')

    new_subelement = SubElement(new_element, 'Provenance').text = 'wikidata'
                
                
    # loop through dictionary
    for key in r_dict:

        # exclude nan values
        if type(r_dict[key]) == str or type(r_dict[key]) == list:

            if key in mapping_dict.keys():

                # try whether nested elements are provided by the mapping dictionary
                try:
                    new_subelement = SubElement(new_element, mapping_dict[key]['SubElement'])

                    # differentiate bewteen list elements and simple strings
                    if type(r_dict[key]) == str:
                        new_subsubelement = SubElement(new_subelement, mapping_dict[key]['SubSubElement']).text = r_dict[key]
                    else:    
                        for value in r_dict[key]:
                            new_subsubelement = SubElement(new_subelement, mapping_dict[key]['SubSubElement']).text = value

                # else only use the direct term - applies if no list of elements expected (ID, conservation status, etc--)
                except:    
                    new_subelement = SubElement(new_element, mapping_dict[key]).text = r_dict[key]


    if save_each_entry == True:       
        # writing XML file out
        tree = ET.ElementTree(root)
        tree.write(getPathXML(), encoding='UTF-8' ,xml_declaration=True, short_empty_elements=False)

    return root


# In[ ]:





# ## Retrieval & Processing
# 
# ### Retrieval of  list of species
# 

# In[ ]:


def getIdentifierList():

    path_ids = getPathIDs()

    # try reading in list of identifiers
    try:
        retrieved_identifiers = list(pd.read_csv(path_ids)['resources'])

        # if list longer 100, consider as full list, not mock, example
        if len(retrieved_identifiers) > 100:
            identifier_list = retrieved_identifiers
        else:
            identifier_list = collectAndSaveIdentifiers()
    except:
        identifier_list = collectAndSaveIdentifiers()
        
    return identifier_list


# In[ ]:





# ### Retrieval of attributes for each species & processing to Dataframe and XML

# In[ ]:


def retrieveAttributesForResources():

    attribute_query = getAttributeQuery()
    
    # read in or create Dataframe    
    df_results = createDataframe()
    
    identifier_list = getIdentifierList()
    
    for identifier in identifier_list:
        
        # check if already queried and saved to df
        if identifier.lower() not in list(df_results['resource']):

            #retrieval of attributes per species
            results = runQuery(attribute_query.format(identifier), JSON)
            
            while type(results) == str:
                print(' - Error at: ', getCurrentTime().strftime("%D - %H:%M:%S"))
                time.sleep(60)
                results = runQuery(attribute_query.format(identifier), JSON)

            # convert and merge results in dict
            r_dict = convertResultsToDict(results)

            # append new entries
            df_results = appendDataframe(r_dict, df_results)

            # try to avoid http error: too many requests
            time.sleep(1)
            
            
        else:
            print('\n*****\n - All identifiers retrieved! - \n*****\n')
            break

    return df_results


# In[ ]:


def retrievalRestarter(end_time):
    # restarts loop of retrieval process iteratively 
    # to avoid periods where the process still runs but no new results are saved
    
    while getCurrentTime() < end_time:
    
        # retrieve results
        retrieveAttributesForResources()
        


# In[ ]:


def createXML(path_XML = getPathXML(), path_Df=getPathDF()):
    
    
    df_results = createDataframe(path_Df)
   
    # convert strings of lists to list of stings
    for column in df_results.columns:
        df_results[column] = df_results[column].apply(lambda s: convertStrToList(s))
    
    # try to read in root, otherwise create
    try:
        tree = ET.parse(path_XML)
        root = tree.getroot()
    except:
        # creation of new root 
        root = ET.Element('Animals_And_Plants')
        
        
    # convert dataframe to list of dictionaries
    # -> one entry corresponds to one dictionary containing attribute-value pairs for one record
    list_of_dicts = df_results.to_dict(orient = 'records')

    # create entry in xml file for each result dictionary
    for r_dict in list_of_dicts:
        
        # append xml file
        root = appendXML(root, r_dict)
        
    
    tree = ET.ElementTree(root)
    tree.write(path_XML, encoding='UTF-8' ,xml_declaration=True, short_empty_elements=False)
        


# In[ ]:





# ## Retrieval of Scientific Names

# In[ ]:


def retrieveTaxonNamesForResources():
    
    
    path_df_scientificNames = '../Data/wikidata/wd_scientificNames.csv'
    
    try: 
        df_results = pd.read_csv(path_df_scientificNames)

    except:
        df_results = pd.DataFrame(columns = ['resource', 'taxonName'])

    
    identifier_list = list(pd.read_csv(getPathDF()).resource)
    
    offset = 0
    chunksize = 100
    
    while offset < len(identifier_list):
        identifier_string = ''
        for resource in identifier_list[offset : offset+chunksize]:
            resource = 'wd:Q' + resource.split('/q')[-1]
            identifier_string += resource + ' '
        
        offset += chunksize

        #retrieval of attributes per species
        results = runQuery(getTaxonNameQuery().format(identifier_string), JSON)

        while type(results) == str:
            print(' - Error at: ', getCurrentTime().strftime("%D - %H:%M:%S"))
            time.sleep(60)
            results = runQuery(getTaxonNameQuery().format(identifier_string), JSON)

        # convert and merge results in dict
        rows = convertResultsToDictTaxonName(results)

        # append new entries
        df_new_entry = pd.DataFrame(rows, columns = ['resource', 'taxonName'])
        df_results = pd.concat([df_results, df_new_entry], ignore_index=True)
        
        df_results.to_csv(path_df_scientificNames, index=False)

        # try to avoid http error: too many requests
        time.sleep(5)
        

    return df_results


# In[ ]:


def convertResultsToDictTaxonName(results):

    rows = []

    # loop through different result sets for this resource
    for result_set in results['results']['bindings']:
        # create new results dict to collect and combine attributes
        r_dict = {}

        # loop through all attributes for this result set
        for attribute in result_set:
            #print(attribute, result_set[attribute]['value'])

            # assign new value (in lower case)
            new_value = result_set[attribute]['value'].lower()

            # if attribute not yet seen, add to dict
            if attribute not in r_dict.keys():
                # assign string to attribute 
                r_dict[attribute] = new_value

            # if attribute already in dict
            else:
                old_value = r_dict[attribute]

                # if value is single string, create list of old and new value and assign back
                if type(old_value) == str and old_value != new_value:
                    l = []
                    l.append(old_value)
                    l.append(new_value)
                    r_dict[attribute] = l

                # if value is list, append new_value
                elif type(old_value) == list and new_value not in old_value:
                    old_value.append(new_value)

        rows.append(r_dict)

    return rows


# In[ ]:


#df_results = retrieveTaxonNamesForResources()
#df_results


# In[ ]:





# In[ ]:




