#ifndef _STACK_H
#define _STATCK_H
#include "Node.h"
#include "StackException.h"
namespace org
{
	namespace bala
	{
  	template <class T> class Stack
   	{
      private:
     		struct node<T>* ptr;

      public:
      	Stack();
        void push(T item);
        T pop() throw (StackException);
        T getCurrentValue() throw (StackException);
        ~Stack();      
   	};

   
  	template<class T> Stack<T> :: Stack()
    {
      ptr = NULL;
    }

    template<class T> void Stack<T> :: push(T data)
    {
      struct node<T>* item = new struct node<T>();
      item->data = data;
      item->next = NULL;
      if(ptr == NULL)
      {
        ptr = item;
      }
      else
      {
        item->next = ptr;
        ptr = item;
      }
    }

   template<class T> T Stack<T> :: pop() throw (StackException)
    {
			if(NULL == ptr)
			{
				throw StackException(EMPTY_STACK);
			}
      T data = ptr->data;
      struct node<T>* item = ptr->next;
      delete ptr;
      ptr = item;
      return data;
    }

    template<class T> T Stack<T> :: getCurrentValue() throw (StackException)
    {
			if(NULL == ptr)
			{
				throw StackException(EMPTY_STACK);
			}
      return ptr->data;   
    } 

    template<class T> Stack<T> :: ~Stack()
    {
      while(NULL != ptr)
      {
        pop();
      }

    }
  
	}  
}
#endif
