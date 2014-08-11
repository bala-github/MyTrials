#include <iostream>
using namespace std;

int main(int argc, char** argv)
{
	unsigned long previous = 1L;
	unsigned long current = 2L;
	unsigned long sum = 0;
	unsigned long temp = 0;
	do
	{
    temp = current + previous;
		if(!(current&1))
		{
			sum = sum + current;
		}
		previous = current;
		current = temp;
	}while(current < 4000000L);
	
	cout << "Sum is:" << sum << endl;
}
