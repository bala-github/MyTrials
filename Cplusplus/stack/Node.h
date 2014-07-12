#ifndef _NODE_H
#define _NODE_H

namespace org
{
	namespace bala
	{
		template <class T> struct node
		{
  		T data;
	    struct node<T>* next;  		 	   	      
  	};
	} 
}
#endif
