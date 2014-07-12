#include <iostream>
#include "Stack.h"

using namespace org::bala;

typedef Stack<int> IntStack;

void push(int data, IntStack& dataStack, IntStack& minStack)
{
		
	dataStack.push(data);
	try
	{
		int currentMin = minStack.getCurrentValue();
		if(data < currentMin)
		{
			minStack.push(data);
		}	
	}
	catch(StackException& e)
	{
		if(e.getErrorCode() == EMPTY_STACK)
		{
			minStack.push(data);
		}
		else
		{
			throw;
		}
	}	
}

int pop(IntStack& dataStack, IntStack& minStack)
{
	int data = dataStack.pop();

	if(data == minStack.getCurrentValue())
	{
			minStack.pop();
	}		

	return data;	
}

int main(int argc, char** argv)
{

  IntStack dataStack, minStack;

	int i = 0, n, data;

  cout << "Enter total count." << endl;

  cin >> n;

  while(i < n)
	{
		cin >> data;
		push(data, dataStack, minStack);
		i++;
	}

	cout << "Minumum Value is:" << minStack.getCurrentValue() << endl;
}
