package jp.legalontech.cabinet;

import com.linecorp.armeria.common.grpc.GrpcSerializationFormats;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.grpc.GrpcService;
import io.grpc.protobuf.services.ProtoReflectionService;
import jp.legalontech.cabinet.application.config.DatabaseConfig;
import jp.legalontech.cabinet.presentation.LoginService;
import jp.legalontech.cabinet.presentation.UserService;
import jp.legalontech.cabinet.presentation.AzureService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static Main instance;
    private SqlSessionFactory sqlSessionFactory;
    private static final int HTTP_PORT = 8080;
    private static final int HTTPS_PORT = 8443;

    public static void main(String[] args) {
        instance = getInstance();
        instance.startServer();
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    private void initialize() {
        sqlSessionFactory = DatabaseConfig.buildSessionFactory();
    }

    private void startServer() {
        LOGGER.info("Server started.");
        final Server server = newServer();
        initialize();
        server.closeOnJvmShutdown().thenRun(() -> LOGGER.info("Server has been stopped."));
        server.start().join();
    }

    private static Server newServer() {
        final ServerBuilder sb = Server.builder();
        sb.http(HTTP_PORT)
            .https(HTTPS_PORT)
            .tlsSelfSigned();
        configureServices(sb);
        return sb.build();
    }

    private static void configureServices(ServerBuilder sb) {
        final GrpcService grpcService =
            GrpcService.builder()
                    .addService(ProtoReflectionService.newInstance())
                    .addService(new LoginService())
                    .addService(new UserService())
                    .addService(new AzureService())
                    .supportedSerializationFormats(GrpcSerializationFormats.values())
                    .enableUnframedRequests(true)
                    // You can set useBlockingTaskExecutor(true) in order to execute all gRPC
                    // methods in the blockingTaskExecutor thread pool.
                    // .useBlockingTaskExecutor(true)
                    .build();
        sb.service(grpcService);
    }
}
