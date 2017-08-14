package gov.cdc.ncezid.eip.repository;

import com.ng.cds.phTriggers.PublicHealthTriggers;
import com.ng.cds.phTriggers.model.RCTCCode;
import com.ng.cds.phTriggers.repository.RCTCCodesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@SpringBootConfiguration
@SpringApplicationConfiguration(classes = PublicHealthTriggers.class)
        public class RCTCCodesRepositoryTest {

    @Autowired
    RCTCCodesRepository repo;

    @Test
    public void readCodes() throws Exception {
        List<RCTCCode> result = repo.getCodesByCategory("Lab obs Test Name");
        printResults(result);
    }

    private void printResults(List<RCTCCode> result) {
        result.stream().forEach(System.out::println);
    }

}