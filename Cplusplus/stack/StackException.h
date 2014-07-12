#ifndef __STACK_EXCEPTION_H
#define __STACK_EXCEPTION_H
#include <exception>

using namespace std;

namespace org
{
	namespace bala
	{
		enum StackErrorCode { EMPTY_STACK };
		class StackException : public exception
		{

			private:
			StackErrorCode m_errorCode;

			public:
			StackException(StackErrorCode errorCode);
			StackErrorCode getErrorCode();
			virtual const char* what() const throw();
		};
	}
}
#endif
