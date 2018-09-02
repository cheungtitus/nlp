# Overview
NLP is a natural language processing microservice made available through RESTful endpoints.

Project is structured in the following modules allowing flexibility to use either web server, and replacement or addition of NLP engine:
* Embedded Tomcat module with Spring MVC
* Embedded Netty module with Spring WebFlux
* NLP module(s) with any NLP engine

# Features
* Sentiment analysis using pre-trained sentiment model packaged with Stanford CoreNLP.

# How To Run In IntelliJ
1. Customize HTTP server port in configs/defaults.conf.
2. Run any of the Run Configurations (eg SpringWebSpringBootTomcat or SpringWebFluxSpringBootNetty).
3. Query any of the following endpoints:

    |Endpoint                                           |Description                                                                    |
    |---------------------------------------------------|-------------------------------------------------------------------------------|
    |http://localhost:< port >/getsentiment/< sentence >|Get one of Positive, Neutral, or Negative rating                               |
    |http://localhost:< port >/check                    |Identify which rest controller is being used to tell if Tomcat or Netty is used |
 
# Outstanding
Following is question posted on upstream inquiring how to generate sentiment model file.

https://github.com/stanfordnlp/CoreNLP/issues/742

# Other Info
## Good Reference
I arrived to the same conclusion long time ago of using bi-directional @OneToMany and @ManyToOne by pain sticking testing.  It is nice to see Vlad Mihalcea documented this in such an easy to understand manner.

https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/

## Missing Library

###### Include EJML
The EMJL library is only included in the compiled library.  If Maven is used the local M2 cache may not have this downloaded as mentioned on the following ticket.
https://github.com/stanfordnlp/CoreNLP/issues/347

## Sentiment Analysis, Evaluation, and Retraining
Per the description on https://nlp.stanford.edu/sentiment/code.html, the following can be done.

#### Sentiment Analysis Using Stanford Trained Model
Analysis can be run on file or interactively.
```java
java -cp "*" -mx5g edu.stanford.nlp.sentiment.SentimentPipeline -file foo.txt 
java -cp "*" -mx5g edu.stanford.nlp.sentiment.SentimentPipeline -stdin
```

Example when running interactively.  Details of how to use this class can be found on its [Javadoc](https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/sentiment/SentimentPipeline.html).
```java
C:\Users\cheun\.m2\repository\edu\stanford\nlp\stanford-corenlp\3.9.1>java -cp "*;C:\Users\cheun\.m2\repository\org\ejml\*" -Xmx5g edu.stanford.nlp.sentiment.SentimentPipeline -stdin
Adding annotator tokenize
No tokenizer type provided. Defaulting to PTBTokenizer.
Adding annotator ssplit
Adding annotator parse
Loading parser from serialized file edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz ... done [0.7 sec].
Adding annotator sentiment
Reading in text from stdin.
Please enter one sentence per line.
Processing will end when EOF is reached.
This sucks.
  Negative
This lib is a memory hog.
  Positive
It's terrible.
  Negative
It's extremely terrible.
  Very negative
I think FB could have been better if it's done in a better way.
  Negative
```

#### Sentiment Model Retraining
CoreNLP comes with model.ser.gz trained with comments from Rotten Tomatoes as described under Sentiment Dataset below.  Here is an example of how to generate a new model using a different data set.
SentimentTraining's Javadoc has further description [here](https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/sentiment/SentimentTraining.html).
```java
java -mx8g edu.stanford.nlp.sentiment.SentimentTraining -numHid 25 -trainPath train.txt -devPath dev.txt -train -model model.ser.gz
```

#### Sentiment Evaluation
Some limited explanation can be found on Evaluate's Javadoc [here](https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/sentiment/Evaluate.html).
Note the Javadoc is poorly written giving the following example which is missing the flags -model and -treebank
```java
java edu.stanford.nlp.sentiment.Evaluate edu/stanford/nlp/models/sentiment/sentiment.ser.gz /u/nlp/data/sentiment/trees/dev.txt
```

The correct example and output is shown below, assuming the file 'test.txt' was extracted from [Train,Dev,Test Splits in PTB Tree Format](https://nlp.stanford.edu/sentiment/trainDevTestTrees_PTB.zip).
```java
C:\Users\cheun\.m2\repository\edu\stanford\nlp\stanford-corenlp\3.9.1>java -cp "stanford-corenlp-3.9.1.jar;stanford-corenlp-3.9.1-models.jar;C:\Users\cheun\.m2\repository\org\ejml\*" -Xmx5g edu.stanford.nlp.sentiment.Evaluate -model edu\stanford\nlp\models\sentiment\sentiment.ser.gz -treebank test.txt
EVALUATION SUMMARY
Tested 82600 labels
  66258 correct
  16342 incorrect
  0.802155 accuracy
Tested 2210 roots
  976 correct
  1234 incorrect
  0.441629 accuracy
Label confusion matrix
      Guess/Gold       0       1       2       3       4    Marg. (Guess)
               0     323     161      27       3       3     517
               1    1294    5498    2245     652     148    9837
               2     292    2993   51972    2868     282   58407
               3      99     602    2283    7247    2140   12371
               4       0       1      21     228    1218    1468
    Marg. (Gold)    2008    9255   56548   10998    3791

               0        prec=0.62476, recall=0.16086, spec=0.99759, f1=0.25584
               1        prec=0.55891, recall=0.59406, spec=0.94084, f1=0.57595
               2        prec=0.88982, recall=0.91908, spec=0.75299, f1=0.90421
               3        prec=0.58581, recall=0.65894, spec=0.92844, f1=0.62022
               4        prec=0.8297, recall=0.32129, spec=0.99683, f1=0.46321
Root label confusion matrix
      Guess/Gold       0       1       2       3       4    Marg. (Guess)
               0      44      39       9       0       0      92
               1     193     451     190     131      36    1001
               2      23      62      82      30       8     205
               3      19      81     101     299     255     755
               4       0       0       7      50     100     157
    Marg. (Gold)     279     633     389     510     399

               0        prec=0.47826, recall=0.15771, spec=0.97514, f1=0.2372
               1        prec=0.45055, recall=0.71248, spec=0.65124, f1=0.55202
               2        prec=0.4, recall=0.2108, spec=0.93245, f1=0.27609
               3        prec=0.39603, recall=0.58627, spec=0.73176, f1=0.47273
               4        prec=0.63694, recall=0.25063, spec=0.96853, f1=0.35971
Approximate Negative label accuracy: 0.646009
Approximate Positive label accuracy: 0.732504
Combined approximate label accuracy: 0.695110
Approximate Negative root label accuracy: 0.797149
Approximate Positive root label accuracy: 0.774477
Combined approximate root label accuracy: 0.785832
class edu.stanford.nlp.sentiment.AbstractEvaluate
```
  
## Sentiment Dataset
The three files below can be downloaded from https://nlp.stanford.edu/sentiment/.

##### [Main zip file with readme (6mb)](http://nlp.stanford.edu/~socherr/stanfordSentimentTreebank.zip) - stanfordSentimentTreebank.zip
###### Content
      1290263  2013-10-10 05:29   stanfordSentimentTreebank/datasetSentences.txt
        83764  2013-10-10 05:30   stanfordSentimentTreebank/datasetSplit.txt
     12010637  2013-10-10 05:40   stanfordSentimentTreebank/dictionary.txt
      1195613  2013-02-02 08:46   stanfordSentimentTreebank/original_rt_snippets.txt
         2357  2013-10-10 05:41   stanfordSentimentTreebank/README.txt
      3263577  2013-10-10 05:41   stanfordSentimentTreebank/sentiment_labels.txt
      1226029  2013-02-02 09:06   stanfordSentimentTreebank/SOStr.txt
      1308918  2013-02-02 09:06   stanfordSentimentTreebank/STree.txt
            0  2013-10-10 07:07   __MACOSX/
            0  2013-10-10 07:07   __MACOSX/stanfordSentimentTreebank/
          171  2013-10-10 05:29   __MACOSX/stanfordSentimentTreebank/._datasetSentences.txt
          229  2013-10-10 05:30   __MACOSX/stanfordSentimentTreebank/._datasetSplit.txt
          171  2013-10-10 05:40   __MACOSX/stanfordSentimentTreebank/._dictionary.txt    
          240  2013-02-02 08:46   __MACOSX/stanfordSentimentTreebank/._original_rt_snippets.txt
          229  2013-10-10 05:41   __MACOSX/stanfordSentimentTreebank/._README.txt
          171  2013-10-10 05:41   __MACOSX/stanfordSentimentTreebank/._sentiment_labels.txt

###### File Descriptions
####### original_rt_snippets.txt
This file contains 10,605 processed comments from the original pool of Rotten Tomatoes HTML files. Some comments may contain multiple sentences on the same line.

####### <a name="datasetsentences">datasetSentences.txt</a>
Once the comments in the original_rt_snippets.txt file have been processed via the Stanford Parser this file will be generated.
Each comment in original_rt_snippets.txt represents a comment as shown on Rotten Tomatoes and may contain multiple sentences per comment.
If there are multiple sentences for a comment, the Stanford Parser will split up the comment so each sentence will be listed on its own line which is why this file has 11,855 lines instead of 10,605 lines.
Each line on this fle contains the sentence index, followed by the sentence string separated by a tab.

####### <a name="datasetsplit">datasetSplit.txt</a>
Each line in datasetSentences.txt is split into three different lists, which are train, test, and dev.
This file maps each line in datasetSentences.txt to a number representing which of these three list it is assigned to. 
* 1 = train
* 2 = test
* 3 = dev

For example 2,1 means the second setence from datasetSentences.txt is assigned to the training set, and 3,2 means the third sentence from datasetSentences.txt is assigned to the test set.
The three files in trainDevTestTrees_PTB.zip below is generated using this mapping.

####### <a name="dictionary">dictionary.txt</a>
The sentences in datasetSentences.txt is broken down into 239,232 phrases, each represented by a line in this file.
Each phrase is listed along with is phrase id, separated by the '|' delimiter.

####### <a name="sentimentlabels">sentiment_labels.txt</a>
Each phrase from dictionary.txt is processed via the Stanford Sentiment Annotator and assigned into a sentiment category below represented by a number from 0 to 1.
This file has 239,232 lines for all the phrases from dictionary.txt represented by a phrase id and a sentiment number separeated by the '|' delimiter. 

|Number Range |Sentiment Category|
| ----------- |------------------|
|[0, 0.2]    |Very negative     |
|(0.2, 0.4]   |Negative          |
|(0.4, 0.6]   |Neutral           |
|(0.6, 0.8]   |Positive          |
|(0.8, 1.0]   |Very positive     |

####### SOStr.txt & STree.txt
SOStr.txt is generated with the Stanford Tokenizer which in essence is breaking up a sentence into its words. 
STree.txt is encoded with the trees in a parent pointer format. Each line corresponds to each sentence in the datasetSentences.txt file.

##### [Train,Dev,Test Splits in PTB Tree Format](https://nlp.stanford.edu/sentiment/trainDevTestTrees_PTB.zip) - trainDevTestTrees_PTB.zip
###### Content
       280825  2013-10-15 16:37   trees/dev.txt
       559614  2013-10-15 16:37   trees/test.txt
      2160058  2013-10-15 16:37   trees/train.txt

###### File Descriptions
These three files are trees generated using [ReadSentimentDataset.java](https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/sentiment/ReadSentimentDataset.html).
Which sentence from [datasetSentences.txt](#datasetsentences) goes into which file is determined by the mapping in [datasetSplit.txt](#datasetsplit).

In order to create a new model file, a training file (eg train.txt) in tree format needs to be created with the following.
```java
java edu.stanford.nlp.sentiment.ReadSentimentDataset -dictionary stanfordSentimentTreebank/dictionary.txt -sentiment stanfordSentimentTreebank/sentiment_labels.txt -tokens stanfordSentimentTreebank/SOStr.txt -parse stanfordSentimentTreebank/STree.txt -split stanfordSentimentTreebank/datasetSplit.txt -train train.txt -dev dev.txt -test test.txt 
```

##### [Dataset raw counts (5mb)](http://nlp.stanford.edu/~socherr/stanfordSentimentTreebankRaw.zip) - stanfordSentimentTreebankRaw.zip
###### Content
      3555660  2013-10-10 07:08   stanfordSentimentTreebankRaw/rawscores_exp12.txt
         1110  2013-10-04 16:33   stanfordSentimentTreebankRaw/README.txt
     12249037  2013-10-10 07:04   stanfordSentimentTreebankRaw/sentlex_exp12.txt

###### File Descriptions
####### rawscores_exp12.txt
Looks like each of the 239,232 phrases can be assigned a set potential sentiment scores from 1 to 25 where 1 is the most negative and 25 is the most positive.  For example the following means phrase 115 has 
generated the sentiment scores 1, 5, and 2.  These will then be averaged and mapped to the range [0,1].   The result will be a number in 
the 5 categories on the table from [sentiment_labels.txt](#sentimentlabels).
```text
115,1,5,2
```

####### sentlex_exp12.txt
Essentially the same file as [dictionary.txt](#dictionary) from stanfordSentimentTreebank.zip.  For example the following means the phrase 
'a big letdown' is identified by phrase id 115.
```text
115,a big letdown
```

# Using edu.stanford.nlp.pipeline.StanfordCoreNLP
The following example shows the full verbose output of each stage in a pipeline of annotators.  

```java
C:\Users\cheun\.m2\repository\edu\stanford\nlp\stanford-corenlp\3.9.1>java -cp "*;C:\Users\cheun\.m2\repository\org\ejml\*" -Xmx2g edu.stanford.nlp.pipeline.StanfordCoreNLP -annotators tokenize,ssplit,pos,parse,sentiment
Adding annotator tokenize
No tokenizer type provided. Defaulting to PTBTokenizer.
Adding annotator ssplit
Adding annotator pos
Loading POS tagger from edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger ... done [1.5 sec].
Adding annotator parse
Loading parser from serialized file edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz ... done [1.4 sec].
Adding annotator sentiment

Entering interactive shell. Type q RETURN or EOF to quit.
NLP> Your service sucks.  I don't get what I paid for.

Sentence #1 (4 tokens, sentiment: Negative):
Your service sucks.

Tokens:
[Text=Your CharacterOffsetBegin=0 CharacterOffsetEnd=4 PartOfSpeech=PRP$ SentimentClass=Neutral]
[Text=service CharacterOffsetBegin=5 CharacterOffsetEnd=12 PartOfSpeech=NN SentimentClass=Neutral]
[Text=sucks CharacterOffsetBegin=13 CharacterOffsetEnd=18 PartOfSpeech=VBZ SentimentClass=Negative]
[Text=. CharacterOffsetBegin=18 CharacterOffsetEnd=19 PartOfSpeech=. SentimentClass=Neutral]

Constituency parse:
(ROOT
  (S
    (NP (PRP$ Your) (NN service))
    (VP (VBZ sucks))
    (. .)))


Sentiment-annotated binary tree:
(ROOT|sentiment=1|prob=0.280
  (NP|sentiment=2|prob=0.971 (PRP$|sentiment=2|prob=0.995 Your) (NN|sentiment=2|prob=0.986 service))
  (@S|sentiment=1|prob=0.328 (VP|sentiment=1|prob=0.463 sucks) (.|sentiment=2|prob=0.997 .)))


Dependency Parse (enhanced plus plus dependencies):
root(ROOT-0, sucks-3)
nmod:poss(service-2, Your-1)
nsubj(sucks-3, service-2)
punct(sucks-3, .-4)

Sentence #2 (9 tokens, sentiment: Negative):
I don't get what I paid for.

Tokens:
[Text=I CharacterOffsetBegin=21 CharacterOffsetEnd=22 PartOfSpeech=PRP SentimentClass=Neutral]
[Text=do CharacterOffsetBegin=23 CharacterOffsetEnd=25 PartOfSpeech=VBP SentimentClass=Neutral]
[Text=n't CharacterOffsetBegin=25 CharacterOffsetEnd=28 PartOfSpeech=RB SentimentClass=Neutral]
[Text=get CharacterOffsetBegin=29 CharacterOffsetEnd=32 PartOfSpeech=VB SentimentClass=Neutral]
[Text=what CharacterOffsetBegin=33 CharacterOffsetEnd=37 PartOfSpeech=WP SentimentClass=Neutral]
[Text=I CharacterOffsetBegin=38 CharacterOffsetEnd=39 PartOfSpeech=PRP SentimentClass=Neutral]
[Text=paid CharacterOffsetBegin=40 CharacterOffsetEnd=44 PartOfSpeech=VBD SentimentClass=Neutral]
[Text=for CharacterOffsetBegin=45 CharacterOffsetEnd=48 PartOfSpeech=IN SentimentClass=Neutral]
[Text=. CharacterOffsetBegin=48 CharacterOffsetEnd=49 PartOfSpeech=. SentimentClass=Neutral]

Constituency parse:
(ROOT
  (S
    (NP (PRP I))
    (VP (VBP do) (RB n't)
      (VP (VB get)
        (SBAR
          (WHNP (WP what))
          (S
            (NP (PRP I))
            (VP (VBD paid)
              (PP (IN for)))))))
    (. .)))


Sentiment-annotated binary tree:
(ROOT|sentiment=1|prob=0.497 (NP|sentiment=2|prob=0.996 I)
  (@S|sentiment=1|prob=0.483
    (VP|sentiment=2|prob=0.551
      (@VP|sentiment=2|prob=0.963 (VBP|sentiment=2|prob=0.992 do) (RB|sentiment=2|prob=0.994 n't))
      (VP|sentiment=2|prob=0.748 (VB|sentiment=2|prob=0.994 get)
        (SBAR|sentiment=2|prob=0.879 (WHNP|sentiment=2|prob=0.994 what)
          (S|sentiment=2|prob=0.943 (NP|sentiment=2|prob=0.996 I)
            (VP|sentiment=2|prob=0.978 (VBD|sentiment=2|prob=0.983 paid) (PP|sentiment=2|prob=0.992 for))))))
    (.|sentiment=2|prob=0.997 .)))


Dependency Parse (enhanced plus plus dependencies):
root(ROOT-0, get-4)
nsubj(get-4, I-1)
aux(get-4, do-2)
neg(get-4, n't-3)
nmod:for(paid-7, what-5)
nsubj(paid-7, I-6)
ccomp(get-4, paid-7)
case(what-5, for-8)
punct(get-4, .-9)
NLP>
```