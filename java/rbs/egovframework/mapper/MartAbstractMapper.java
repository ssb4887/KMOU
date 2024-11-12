package rbs.egovframework.mapper;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MART DB
 * @author user
 *
 */
public abstract class MartAbstractMapper extends RbsAbstractMapper{
	
	@Resource(name = "martSqlSession")
	public void setSqlSessionFactory(SqlSessionFactory sqlSession) {
		super.setSqlSessionFactory(sqlSession);
	}
}