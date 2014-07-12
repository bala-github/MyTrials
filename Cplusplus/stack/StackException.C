#include "StackException.h"

namespace org
{
	namespace bala
	{
		StackException :: StackException(StackErrorCode errorCode) : m_errorCode(errorCode)
		{
			
		}

		StackErrorCode StackException :: getErrorCode()
		{
			return m_errorCode;
		}

		const char* StackException :: what() const throw()
		{
			switch(m_errorCode)
			{
				case EMPTY_STACK:
					return "Empty Stack";
				default:
					return "UnKnown Error";
			}
		}
	}
}
