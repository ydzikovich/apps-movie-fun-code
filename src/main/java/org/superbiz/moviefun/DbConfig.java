package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
        jpaVendorAdapter.setGenerateDdl(true);
        return jpaVendorAdapter;
    }


    private static DataSource buildDataSource(String url, String username, String password) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    private static LocalContainerEntityManagerFactoryBean buildEntityManagerFactoryBean(DataSource dataSource, HibernateJpaVendorAdapter jpaVendorAdapter, String unitName) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setPackagesToScan(DbConfig.class.getPackage().getName());
        factoryBean.setPersistenceUnitName(unitName);
        return factoryBean;
    }


    @Configuration
    public static class Movies {
        @Value("${moviefun.datasources.movies.url}") String url;
        @Value("${moviefun.datasources.movies.username}") String username;
        @Value("${moviefun.datasources.movies.password}") String password;

        @Bean
        public DataSource moviesDataSource() {
            return buildDataSource(url, username, password);
        }

        @Bean
        LocalContainerEntityManagerFactoryBean moviesEntityManagerFactoryBean(DataSource moviesDataSource, HibernateJpaVendorAdapter jpaVendorAdapter) {
            return buildEntityManagerFactoryBean(moviesDataSource, jpaVendorAdapter, "movies");
        }
    }

    @Configuration
    public static class Albums {
        @Value("${moviefun.datasources.albums.url}") String url;
        @Value("${moviefun.datasources.albums.username}") String username;
        @Value("${moviefun.datasources.albums.password}") String password;

        @Bean
        public DataSource albumsDataSource() {
            return buildDataSource(url, username, password);
        }

        @Bean
        LocalContainerEntityManagerFactoryBean albumsEntityManagerFactoryBean(DataSource albumsDataSource, HibernateJpaVendorAdapter jpaVendorAdapter) {
            return buildEntityManagerFactoryBean(albumsDataSource, jpaVendorAdapter, "albums");
        }
    }
}
