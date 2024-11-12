package rbs.egovframework.mapper;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MART DB(DEV)
 * @author user
 *
 */
public abstract class MartAbstractDevMapper extends RbsAbstractMapper{
	
	@Resource(name = "martDevSqlSession")
	public void setSqlSessionFactory(SqlSessionFactory sqlSession) {
		super.setSqlSessionFactory(sqlSession);
	}
}