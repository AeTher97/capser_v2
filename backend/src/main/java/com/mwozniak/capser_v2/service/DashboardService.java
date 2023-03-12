package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.models.database.Post;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.game.single.SoloGame;
import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import com.mwozniak.capser_v2.models.database.game.team.DoublesGame;
import com.mwozniak.capser_v2.repository.BlobPostRepository;
import com.mwozniak.capser_v2.service.game.DoublesService;
import com.mwozniak.capser_v2.service.game.EasyCapsGameService;
import com.mwozniak.capser_v2.service.game.SinglesGameService;
import com.mwozniak.capser_v2.service.game.UnrankedGameService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DashboardService {

    private final SinglesGameService singlesGameService;
    private final DoublesService doublesService;
    private final EasyCapsGameService easyCapsGameService;
    private final UnrankedGameService unrankedGameService;

    private final BlobPostRepository blobPostRepository;


    public DashboardService(SinglesGameService singlesGameService,
                            DoublesService doublesService,
                            EasyCapsGameService easyCapsGameService,
                            UnrankedGameService unrankedGameService,
                            BlobPostRepository blobPostRepository) {
        this.singlesGameService = singlesGameService;
        this.doublesService = doublesService;
        this.easyCapsGameService = easyCapsGameService;
        this.unrankedGameService = unrankedGameService;
        this.blobPostRepository = blobPostRepository;
    }

    public List<AbstractGame> getDashboardGames() {
        List<SoloGame> singlesGames = singlesGameService.listAcceptedGames(PageRequest.of(0, 10, Sort.by("time").descending())).getContent();
        List<DoublesGame> doublesGames = doublesService.listAcceptedGames(PageRequest.of(0, 10, Sort.by("time").descending())).getContent();
        List<EasyCapsGame> easyCapsGames = easyCapsGameService.listAcceptedGames(PageRequest.of(0, 10, Sort.by("time").descending())).getContent();
        List<UnrankedGame> unrankedGames = unrankedGameService.listAcceptedGames(PageRequest.of(0, 10, Sort.by("time").descending())).getContent();

        List<AbstractGame> aggregatedList = new ArrayList<>();
        aggregatedList.addAll(singlesGames);
        aggregatedList.addAll(doublesGames);
        aggregatedList.addAll(easyCapsGames);
        aggregatedList.addAll(unrankedGames);


        aggregatedList.sort(AbstractGame.Comparators.DATE);
        Collections.reverse(aggregatedList);
        if (aggregatedList.size() > 10) {
            return aggregatedList.subList(0, 9);
        } else {
            return aggregatedList;
        }
    }

    public List<Post> getAllBlogPosts() {
        return blobPostRepository.findAll(Sort.by("date").descending());
    }

}
