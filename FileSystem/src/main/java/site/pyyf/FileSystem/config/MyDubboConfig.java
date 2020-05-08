package site.pyyf.FileSystem.config;

import com.alibaba.dubbo.config.ServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pyyf.FileSystem.service.IFileSystemService;

@Configuration
public class MyDubboConfig {

	@Bean
	public ServiceConfig<IFileSystemService> userServiceConfig(IFileSystemService fileSystemService){
		ServiceConfig<IFileSystemService> serviceConfig = new ServiceConfig<>();
		serviceConfig.setInterface(IFileSystemService.class);
		serviceConfig.setRef(fileSystemService);
		serviceConfig.setVersion("1.0.0");

		return serviceConfig;
	}

}
