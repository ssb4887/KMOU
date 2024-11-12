package rbs.egovframework.social.kakao.api.impl.json;

import rbs.egovframework.social.kakao.api.User;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class KakaoModule extends SimpleModule {
	private static final long serialVersionUID = 1L;

	public KakaoModule() {
		super("KakaoModule");
	}

	public void setupModule(Module.SetupContext context) {
		context.setMixInAnnotations(User.class, UserMixin.class);
	}
}
