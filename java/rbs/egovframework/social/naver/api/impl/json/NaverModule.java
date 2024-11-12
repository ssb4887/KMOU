package rbs.egovframework.social.naver.api.impl.json;

import rbs.egovframework.social.naver.api.User;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class NaverModule extends SimpleModule {
	private static final long serialVersionUID = 1L;

	public NaverModule() {
		super("NaverModule");
	}

	public void setupModule(Module.SetupContext context) {
		context.setMixInAnnotations(User.class, UserMixin.class);
	}
}
