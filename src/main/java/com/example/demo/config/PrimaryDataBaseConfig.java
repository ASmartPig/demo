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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Description: 主数据源配置类
 */
@Configuration
// 前缀为primary.datasource.druid的配置信息
//@ConfigurationProperties(prefix = "primary.datasource.druid")
@MapperScan(basePackages = PrimaryDataBaseConfig.PACKAGE, sqlSessionFactoryRef = "primarySqlSessionFactory")
public class PrimaryDataBaseConfig {

    /**
     * dao层的包路径
     */
    static final String PACKAGE = "com.example.demo.mapper";

    /**
     * mapper文件的相对路径
     */
    private static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

//    private String filters;
//    private String url;
//    private String username;
//    private String password;
//    private String driverClassName;
//    private int initialSize;
//    private int minIdle;
//    private int maxActive;
//    private long maxWait;
//    private long timeBetweenEvictionRunsMillis;
//    private long minEvictableIdleTimeMillis;
//    private String validationQuery;
//    private boolean testWhileIdle;
//    private boolean testOnBorrow;
//    private boolean testOnReturn;
//    private boolean poolPreparedStatements;
//    private int maxPoolPreparedStatementPerConnectionSize;


    // 主数据源使用@Primary注解进行标识
    @Primary
    @Bean(name = "primaryDataSource")
    public DataSource primaryDataSource() throws SQLException {
        DruidDataSource druid = new DruidDataSource();
        // 监控统计拦截的filters
        druid.setFilters("stat");

        // 配置基本属性
        druid.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druid.setUsername("ahwd_MF");
        druid.setPassword("Riec0shu");
        druid.setUrl("jdbc:mysql://47.92.148.144:3306/db_5e3f3fe2e06e8e246beb9d7b3a67fee4?useUnicode=true&characterEncoding=utf-8&userSSL=false&serverTimezone=GMT%2B8");

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

    // 创建该数据源的事务管理
    @Primary
    @Bean(name = "primaryTransactionManager")
    public DataSourceTransactionManager primaryTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(primaryDataSource());
    }

    // 创建Mybatis的连接会话工厂实例
    @Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource primaryDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(primaryDataSource);  // 设置数据源bean
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(PrimaryDataBaseConfig.MAPPER_LOCATION));  // 设置mapper文件路径

        return sessionFactory.getObject();
    }

}