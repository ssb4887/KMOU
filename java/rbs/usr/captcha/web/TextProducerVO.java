package rbs.usr.captcha.web;

import nl.captcha.text.producer.TextProducer;

public class TextProducerVO implements TextProducer{
    
	private final String answer;

	public TextProducerVO(String answer){    
		this.answer = answer; 
	}

    @Override
	public String getText() {        
    	return answer;
	}
}