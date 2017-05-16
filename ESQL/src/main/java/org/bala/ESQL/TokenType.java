package org.bala.ESQL;

public enum TokenType {

	START,
	FIELD,
	FROM,
	SELECT,
	UPDATE,
	DELETE,
	DESC,
	TABLE_NAME,
	TERMINATOR,
	VALUE,
	WHERE,
	CONDITONAL_OPEARATOR,
	GROUP_OPEN,
	GROUP_CLOSE,
	LOGICAL_OPERATOR,
	ASSIGNMENT_OPERATOR,
	SET,
	ORDER,
	BY,
	DIRECTION,
	ASC,
	DSC,
	GROUP,
	SUM,
	AVG;
	
	public String toString(){
		
		if(name().equalsIgnoreCase(TokenType.GROUP_OPEN.name())){
			return "(";
		}else if(name().equalsIgnoreCase(TokenType.GROUP_CLOSE.name())){
			return ")";
		}else if(name().equalsIgnoreCase(TokenType.TERMINATOR.name())){
			return ";";
		}else{
			return name();
		}
	}
}
