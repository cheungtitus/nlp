package com.kenrui.nlp.taxonomy;

import com.kenrui.nlp.common.entities.Model;
import com.kenrui.nlp.common.entities.Taxonomy;
import com.kenrui.nlp.common.repositories.ModelRepository;
import com.kenrui.nlp.common.repositories.TaxonomyCategoryRepository;
import com.kenrui.nlp.common.repositories.TaxonomyProductRepository;
import com.kenrui.nlp.taxonomy.config.AppConfigTaxonomyModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
//@ContextConfiguration(classes = AppConfigTaxonomyModelWrapper.class)
public class TaxonomyModelWrapper {
    @Autowired
    public String pythonLibPath;
    protected TaxonomyModel taxonomyModel1InsuranceAuto;
    protected TaxonomyModel taxonomyModel2InsuranceLife;
    protected TaxonomyModel taxonomyModel3MortgageHome;
    protected List<TaxonomyModel> taxonomyModelList;
    protected ExecutorService executorService;

    public TaxonomyModelWrapper(ModelRepository modelRepository,
                                TaxonomyCategoryRepository taxonomyCategoryRepository,
                                TaxonomyProductRepository taxonomyProductRepository) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfigTaxonomyModelWrapper.class);
        System.out.println(pythonLibPath);
        taxonomyModelList = new ArrayList<>();
        Model model1 = new Model("model1");
        Model model2 = new Model("model2");
        Model model3 = new Model("model3");
        String script = "C:\\Users\\cheun\\IdeaProjects\\nlp\\taxonomy\\src\\main\\resources\\taxonomy_rrn.py";

//        taxonomyModel1InsuranceAuto = new TaxonomyModel1InsuranceAuto(script, model1);
        taxonomyModel1InsuranceAuto = new TaxonomyModel1InsuranceAuto(script, model1,
                modelRepository, taxonomyCategoryRepository, taxonomyProductRepository);
        taxonomyModelList.add(taxonomyModel1InsuranceAuto);

//        taxonomyModel2InsuranceLife = new TaxonomyModel2InsuranceLife(script, model2);
        taxonomyModel2InsuranceLife = new TaxonomyModel2InsuranceLife(script, model2,
                modelRepository, taxonomyCategoryRepository, taxonomyProductRepository);
        taxonomyModelList.add(taxonomyModel2InsuranceLife);

//        taxonomyModel3MortgageHome = new TaxonomyModel3MortgageHome(script, model3);
        taxonomyModel3MortgageHome = new TaxonomyModel3MortgageHome(script, model3,
                modelRepository, taxonomyCategoryRepository, taxonomyProductRepository);
        taxonomyModelList.add(taxonomyModel3MortgageHome);

        executorService = Executors.newFixedThreadPool(taxonomyModelList.size());
    }

    public List<Taxonomy> getTaxonomyFromModels(String comment) {
        List<Taxonomy> taxonomyList = new ArrayList<>();
        List<Callable<Taxonomy>> callableList = new ArrayList<>();

        taxonomyModelList.forEach((taxonomyModel) -> {
            Callable<Taxonomy> task = () -> taxonomyModel.getTaxonomy(comment);
            callableList.add(task);
        });

        try {
            executorService.invokeAll(callableList)
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            throw new IllegalStateException(e);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            throw new IllegalStateException(e);
                        }
                    }).forEach(taxonomyList::add);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return taxonomyList;
    }
}
