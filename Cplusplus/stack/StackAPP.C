#include <iostream>
#include "Stack.h"

using namespace std;
using namespace org::bala;

int main(int argc, char** argv)
{

  typedef Stack<int> IntStack;

  IntStack stack1;

  stack1.push(5);

  stack1.push(6);

  cout << stack1.pop();
}
