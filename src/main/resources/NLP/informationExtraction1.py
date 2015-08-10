#!/usr/bin/python
'''
Created on 12-Feb-2013

@author: mebin
'''
from nltk import pos_tag, word_tokenize
from nltk.corpus import conll2007, gutenberg, ieer
from nltk.tag.stanford import StanfordNERTagger
import nltk
import re
from nltk.util import trigrams
import sys

mergedSpaceSeperatorConstant = '-';
stanfordLocation = []
stanfordDate = []
baseLocation='/usr/local/etc' # this would change on server
line = sys.argv[1]

#Chaining functions together
def ie_preprocess(document):
    s = nltk.sent_tokenize(document)
    s = [nltk.word_tokenize(sent) for sent in s]
    s = [nltk.pos_tag(sent) for sent in s]
    return s

# Named entity recognition
previousDateLoc = ''
def findNamedEntity():
    sentence = ''
    previousDateLoc = 0
    stanfordNERExtractedLines = stanfordNERExtractor(line)
    for stanfordNERExtractedTuple in stanfordNERExtractedLines:
        if stanfordNERExtractedTuple[1] == 'LOCATION':
            stanfordLocation.append(stanfordNERExtractedTuple[0])
        if stanfordNERExtractedTuple[1] == 'DATE':
            if line.find(stanfordNERExtractedTuple[0]) == previousDateLoc:
                stanfordDate.insert((len(stanfordDate) - 1), stanfordDate.pop(len(stanfordDate) - 1) + mergedSpaceSeperatorConstant +stanfordNERExtractedTuple[0])
                print stanfordNERExtractedTuple[0]
            else:
                print stanfordNERExtractedTuple[0]
                stanfordDate.append(stanfordNERExtractedTuple[0])
            previousDateLoc = line.find(stanfordNERExtractedTuple[0]) + len(stanfordNERExtractedTuple[0]) + 1
    sentence += line

    listPreprocessed = ie_preprocess(sentence)
    namedEntityList = []
    chunkedList = []
    for listPreprocess in listPreprocessed:
        chunkedList = nltk.ne_chunk(listPreprocess)
        class doc():
            pass
        doc.headline=['foo']
        doc.text=chunkedList
        IN = re.compile (r'in')#.*\bin\b(?!\b.+ing)
        for i in range(0,len(chunkedList)):
            if type(chunkedList[i]) is nltk.tree.Tree:
                namedEntityList.insert(i, chunkedList[i])
    return namedEntityList


def stanfordNERExtractor(sentence):
    st = StanfordNERTagger(baseLocation + 'english.muc.7class.distsim.crf.ser.gz', baseLocation + 'stanford-ner.jar')
    return st.tag(sentence.split())

findNamedEntity()
fileToCommunicate = open('/home/mebin/Desktop/Lawrence/locationsDate.txt', 'w+')

print stanfordLocation
print stanfordDate
