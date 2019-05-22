package io.pivotal.demos1tparis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
class VoteController {

    //VoteController(VoteRepository voteRepository) {
    //    this.voteRepository = voteRepository;
    //}

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private DataSource dataSource;

    @GetMapping(value = "vote")
    public Iterable<Vote> getVotes() {
        return voteRepository.findAll();
    }


    @GetMapping(value = "config")
    public Map<String, Object> getConfig() throws SQLException {
        final Map<String, Object> config = new HashMap<>();
        try (final Connection conn = dataSource.getConnection()) {
            config.put("persistentDb", !"H2".equals(conn.getMetaData().getDatabaseProductName()));
        }

        return config;
    }

    @PostMapping(value = "vote")
    public Vote createVote(@RequestBody Vote vote) {
        System.out.println(vote);
        return voteRepository.save(vote);
    }
}