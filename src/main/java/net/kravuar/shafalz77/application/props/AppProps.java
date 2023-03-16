package net.kravuar.shafalz77.application.props;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "app")
@RequiredArgsConstructor
@ConstructorBinding
@Getter
public class AppProps {
    private final int bufferSize;
}
