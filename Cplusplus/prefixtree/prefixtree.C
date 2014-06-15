#include <iostream>
using namespace std;
 
typedef struct trie
{
        /*Count of no. of times a word was entered */ 
        int words;
        /*Count of no. of words with this prefix*/
        int prefixes;
          
        char c;
 
        /*edges representing each character*/
        struct trie *edges[26];
}trie;

/*
 *  initialize a tie node.
 *  count and prefix will be zero.
 *  all the edges will be null.
 */ 
trie * initialize(trie *node)
{
        if(node==NULL)
        {
                node = new trie;
                node->words = 0;
                node->prefixes = 0;
                node->c = '\0';
                int i;
                for(i=0;i<26;i++)
                        node->edges[i]=NULL;
                return node;
        }
 
}

/*
 *  add a word to the prefixtree.
 */

trie* addWord(trie *ver,const char *str)
{

  if(str[0] == '\0')
  {
     ver->words = ver->words + 1;
     return ver;
  }

  /*
   * Increment the no. of words with this prefix.
   */    
   ver->prefixes=(ver->prefixes)+1;
   char k;
   k=str[0];
               
   int index=k-'a';
   if(ver->edges[index]==NULL)
   {
       /* No node for the current character. Create a trie node */

       ver->edges[index]=initialize(ver->edges[index]);
       ver->edges[index]->c = str[0];  
   }
   /*Add the remaining characters to approriate edge */
   ver->edges[index] = addWord(ver->edges[index],str + 1) ;
   return ver;
}

/*
 * returns no. of times a word was entered
 */
 

int countWords(trie *ver, const char *str)
{
        if(str[0]=='\0')
        {
                /*reached end of word*/
                return ver->words;
        }
        else
        {
                int k=str[0]-'a';
                if(ver->edges[k]==NULL)
                {
                     /*No. edge with the given character found.*/  
                     return 0;
                } 
                return countWords(ver->edges[k], str + 1);
        }
}

/*
 * returns no. of words  with the given  prefix string. 
 */ 
int countPrefix(trie *ver, const char *str)
{
        if(str[0]=='\0')
        {
                /*reached end of prefix string*/ 
                return ver->prefixes;
        } 
        else
        {
                int k=str[0]-'a';
                if(ver->edges[k]==NULL)
                {
                   /*no edge for this character in the prifix string */
                        return 0;
                } 
                return countPrefix(ver->edges[k], str + 1);
        }
}

void displayWords(trie *ver, string prefix)
{

	if(ver->words > 0)
  {
     cout << endl << prefix.c_str();
  } 
     
  for(int i=0;i<26;i++)
  {
    if(ver->edges[i] != NULL)
    {
      displayWords(ver->edges[i], prefix + ver->edges[i]->c);        
    }  
  }
}

trie* getPrefix(trie* ver, const char* prefix)
{

  if(prefix[0]=='\0')
  {
    /*reached end of prefix string*/ 
    return ver;
  } 
  else
  {
    int k=prefix[0]-'a';
    if(ver->edges[k]==NULL)
    {
      /*no edge for this character in the prifix string */
      return NULL;
    } 
    return getPrefix(ver->edges[k], prefix + 1);
  }
}
  
int main()
{
        trie *start=NULL;
        start=initialize(start);
        int ch=1;
        while(ch)
        {
 
                cout << endl << "1. Insert a word ";
                cout << endl << "2. Count words";
                cout << endl << "3. Count prefixes";
                cout << endl << "4. Display words matching prefix"; 
                cout << endl << "0. Exit" << endl;
                cout << endl << "Enter your choice: ";
                cin >> ch;
                string input;
                switch(ch)
                {
                        case 1:
                                cout << endl << "Enter a word to insert: ";
                                cin >> input; 
                                start = addWord(start,input.c_str());
                                break;
                        case 2:
                                cout << endl << "Enter a word to count words: ";
                                cin >> input;
                                cout << endl << countWords(start,input.c_str());
                                break;
                        case 3:
                                cout << endl << "Enter a word to count prefixes: ";
                                cin >> input;
                                cout << endl << countPrefix(start,input.c_str());
                                break;

                        case 4:
                               cout << endl << "Enter prefix to search";
                               cin >> input;
                               cout << endl << "Words matching prefix" << endl;
                               trie* startFrom = getPrefix(start, input.c_str());
                               //input[input.length() - 1] = '\0';
                               cout << endl << startFrom->c << "-" << input.c_str();  
                               if(startFrom != NULL)
                               { 
                                 displayWords(startFrom, input);
                               } 
                               break; 
                }
    }
    
    return 0;
}
