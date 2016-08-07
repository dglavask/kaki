package alphabit.parser.bnf;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Tokenizer {

	private LinkedList<TokenInfo> tokenInfos;
	private LinkedList<Token> tokens;

	public Tokenizer() {
		tokenInfos = new LinkedList<TokenInfo>();
		tokens = new LinkedList<Token>();
	}

	public void add(String regex, int token) {
		tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex + ")"), token));
	}

	private class TokenInfo {
		public final Pattern regex;
		public final int token;

		public TokenInfo(Pattern regex, int token) {
			super();
			this.regex = regex;
			this.token = token;
		}
	}

	public static class Token {
		public static final int EPSILON=0;
		public static final int NONTERMINAL=1;
		public static final int TERMINAL=2;
		public static final int PRODUCTION=3;
		public static final int OR=4;
		public static final int AND=5;
		public static final int ENDOFSTATEMENT=6;
		
		public final int token;
		public final String sequence;

		public Token(int token, String sequence) {
			super();
			this.token = token;
			this.sequence = sequence;
		}
	}

	private String prepareString(String str){
		return str.replaceAll("\\s+","");
	}
	
	public void tokenize(String str) throws ParseException {
		String s = new String(prepareString(str));
		tokens.clear();
		while (!s.equals("")) {
			boolean match = false;

			for (TokenInfo info : tokenInfos) {
				Matcher m = info.regex.matcher(s);
				if (m.find()) {
					match = true;

					String tok = m.group().trim();
					tokens.add(new Token(info.token, tok));

					s = m.replaceFirst("");
					break;
				}
			}

			if (!match)
				throw new ParseException("Unexpected character in input: " + s, 1);
		}
	}

	public LinkedList<Token> getTokens() {
		return tokens;
	}
}
