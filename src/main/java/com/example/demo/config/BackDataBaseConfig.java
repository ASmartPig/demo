package com.example.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Description: 后台数据源配置类
 */

@Data
@Configuration
//@ConfigurationProperties(prefix = "back.datasource.druid")
@MapperScan(basePackages = BackDataBaseConfig.PACKAGE, sqlSessionFactoryRef = "backSqlSessionFactory")
public class BackDataBaseConfig {

    /**
     * dao层的包路径
     */
    static final String PACKAGE = "com.example.demo.dao";

    /**
     * mapper文件的相对路径
     */
    private static final String MAPPER_LOCATION = "classpath:dao/*.xml";


    @Bean(name = "backDataSource")
    public DataSource backDataSource() throws SQLException {
        DruidDataSource druid = new DruidDataSource();
        // 配置基本属性
        druid.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druid.setUsername("root");
        druid.setPassword("root");
        druid.setUrl("jdbc:mysql://47.105.173.228:3306/userinfo?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&autoReconnect=true&useSSL=false");

        //初始化时建立物理连接的个数
        druid.setInitialSize(1);
        //最大连接池数量
        druid.setMaxActive(20);
        //最小连接池数量
        druid.setMinIdle(3);
        //获取连接时最大等待时间，单位毫秒。
        druid.setMaxWait(60000);
        //间隔多久进行一次检测，检测需要关闭的空闲连接
        druid.setTimeBetweenEvictionRunsMillis(60000);
        //一个连接在池中最小生存的时间
        druid.setMinEvictableIdleTimeMillis(300000);
        //用来检测连接是否有效的sql
        druid.setValidationQuery("SELECT 'x'");
        //建议配置为true，不影响性能，并且保证安全性。
        druid.setTestWhileIdle(true);
        //申请连接时执行validationQuery检测连接是否有效
        druid.setTestOnBorrow(false);
        druid.setTestOnReturn(false);
        //是否缓存preparedStatement，也就是PSCache，oracle设为true，mysql设为false。分库分表较多推荐设置为false
        druid.setPoolPreparedStatements(false);
        // 打开PSCache时，指定每个连接上PSCache的大小
        druid.setMaxPoolPreparedStatementPerConnectionSize(20);

        return druid;
    }

    @Bean(name = "backTransactionManager")
    public DataSourceTransactionManager backTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(backDataSource());
    }

    @Bean(name = "backSqlSessionFactory")
    public SqlSessionFactory backSqlSessionFactory(@Qualifier("backDataSource") DataSource backDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(backDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(BackDataBaseConfig.MAPPER_LOCATION));

        return sessionFactory.getObject();
    }
}