package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.models.database.Post;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DashboardService {

    private final SinglesRepository singlesRepository;
    private final DoublesRepository doublesRepository;
    private final EasyCapsRepository easyCapsRepository;
    private final UnrankedRepository unrankedRepository;

    private final BlobPostRepository blobPostRepository;


    public DashboardService(SinglesRepository singlesRepository,
                            DoublesRepository doublesRepository,
                            EasyCapsRepository easyCapsRepository,
                            UnrankedRepository unrankedRepository,
                            BlobPostRepository blobPostRepository) {
        this.singlesRepository = singlesRepository;
        this.doublesRepository = doublesRepository;
        this.easyCapsRepository = easyCapsRepository;
        this.unrankedRepository = unrankedRepository;
        this.blobPostRepository = blobPostRepository;
    }

    public List<AbstractGame> getDashboardGames() {
        List<AbstractGame> singlesGames = (List<AbstractGame>) (List<?>) singlesRepository.findSinglesGamesByAcceptedTrue(PageRequest.of(0, 10, Sort.by("time").descending())).getContent();
        List<AbstractGame> doublesGames = (List<AbstractGame>) (List<?>) doublesRepository.findDoublesGameByAcceptedTrue(PageRequest.of(0, 10, Sort.by("time").descending())).getContent();
        List<AbstractGame> easyCapsGames = (List<AbstractGame>) (List<?>) easyCapsRepository.findEasyCapsGamesByAcceptedTrue(PageRequest.of(0, 10, Sort.by("time").descending())).getContent();
        List<AbstractGame> unrankedGames = (List<AbstractGame>) (List<?>) unrankedRepository.findUnrankedGameByAcceptedTrue(PageRequest.of(0, 10, Sort.by("time").descending())).getContent();

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

    public List<Post> getAllBlogPosts(){
        return blobPostRepository.findAll( Sort.by("date").descending());
    }

}
