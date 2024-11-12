package rbs.egovframework.mapper;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MART DB
 * @author user
 *
 */
public abstract class OceanAbstractMapper extends RbsAbstractMapper{
	
	@Resource(name = "oceanSqlSession")
	public void setSqlSessionFactory(SqlSessionFactory sqlSession) {
		super.setSqlSessionFactory(sqlSession);
	}
}