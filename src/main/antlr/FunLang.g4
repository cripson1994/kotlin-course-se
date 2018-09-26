grammar FunLang;

fragment DIGIT : '0'..'9'+ ;
TDIGIT : ('0' | ('1' .. '9') DIGIT*) ;

fragment LETTER : 'A'..'Z' | 'a'..'z' ;

TIF      : 'if' ;
TELSE    : 'else' ;
TWHILE   : 'while';
TFUN     : 'fun' ;
TVAR     : 'var' ;
TRETURN  : 'return' ;
TPRINT   : 'println' ;

TAS    : '=' ;
TMINUS  : '-' ;
TPLUS   : '+' ;
TMULT   : '*' ;
TDIV    : '/' ;
TMOD    : '%' ;
TEQC    : '==' ;
TNEQ    : '!=';
TLT     : '<' ;
TGT     : '>';
TLE     : '<=' ;
TGE     : '>=' ;
TAND    : '&&' ;
TOR     : '||' ;

IDENT : (LETTER | '_') (LETTER | '_' | DIGIT)* ;

WS : ('\t' | '\r'? '\n' | ' ')+ -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN) ;
MULTY_COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;

file : block EOF ;
block : (statement)* ;
block_with_braces : '{' block '}' ;
statement : function | variable | expression | while_expr | if_expr | assignment | return_expr | println ;
function : TFUN IDENT '(' parameter_names ')' block_with_braces ;
variable : TVAR IDENT (TAS expression)? ;
parameter_names : ( | IDENT (',' IDENT)*) ;
while_expr : TWHILE '(' expression ')' block_with_braces ;
if_expr : TIF '(' expression ')' block_with_braces (TELSE block_with_braces)? ;
assignment : IDENT TAS expression ;
return_expr : TRETURN expression ;
expression : function_call | arithm_expression;
println : TPRINT '(' arguments ')' ;
function_call : IDENT '(' arguments ')' ;
arguments : ( | expression (',' expression)* ) ;

arithm_expression : lvalue=arithm_expression  op=arithm_prior1 rvalue=arithm_expression  #ArithmeticPrior1BinaryExpr
                  | lvalue=arithm_expression  op=arithm_prior2 rvalue=arithm_expression   #ArithmeticPrior2BinaryExpr
                  | lvalue=arithm_expression  op=compare_op rvalue=arithm_expression  #LogicalCompareExpr
                  | lvalue=arithm_expression  op=logic_op rvalue=arithm_expression  #LogicalBinaryExpr
                  | '(' arithm_expression ')' #ArithmeticParensExpr
                  | (function_call | TDIGIT | IDENT) #ArithmeticAtomExpr;

arithm_prior1 : TMULT | TDIV | TMOD ;
arithm_prior2 : TPLUS | TMINUS ;
compare_op : TEQC | TNEQ | TLT | TGT | TLE | TGE ;
logic_op : TAND | TOR ;




