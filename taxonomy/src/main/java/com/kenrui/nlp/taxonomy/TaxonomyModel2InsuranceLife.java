package com.kenrui.nlp.taxonomy;

import com.kenrui.nlp.common.entities.Model;
import com.kenrui.nlp.common.entities.Taxonomy;
import com.kenrui.nlp.common.entities.TaxonomyCategory;
import com.kenrui.nlp.common.entities.TaxonomyProduct;
import com.kenrui.nlp.common.repositories.ModelRepository;
import com.kenrui.nlp.common.repositories.TaxonomyCategoryRepository;
import com.kenrui.nlp.common.repositories.TaxonomyProductRepository;
import org.python.util.PythonInterpreter;

import java.util.Properties;

public class TaxonomyModel2InsuranceLife implements TaxonomyModel {
    private ModelRepository modelRepository;
    private TaxonomyCategoryRepository taxonomyCategoryRepository;
    private TaxonomyProductRepository taxonomyProductRepository;

    private String script;
    private Model model;
    private PythonInterpreter interp;

    public TaxonomyModel2InsuranceLife(String script, Model model,
                                       ModelRepository modelRepository,
                                       TaxonomyCategoryRepository taxonomyCategoryRepository,
                                       TaxonomyProductRepository taxonomyProductRepository) {
        this.modelRepository = modelRepository;
        this.taxonomyCategoryRepository = taxonomyCategoryRepository;
        this.taxonomyProductRepository = taxonomyProductRepository;

        this.script = script;
        this.model = modelRepository.findByModel(model.getModel());
        Properties props = new Properties();
        // Per following there are a few important environment variables ...
        // https://docs.python.org/2/using/cmdline.html#environment-variables
        // PYTHONHOME (python.home) is path to the Python libraries
        // PYTHONPATH (python.path) is path to Python modules (*.py source or *.class compiled via Jython)
        String pythonLib = "C:\\Users\\cheun\\IdeaProjects\\nlp\\taxonomy\\src\\main\\resources\\Lib";
        props.setProperty("python.home",pythonLib);
        props.setProperty("python.path",pythonLib);
        props.setProperty("python.prefix", pythonLib);
        PythonInterpreter.initialize(System.getProperties(),props,new String[0]);
        interp = new PythonInterpreter();
        interp.exec("import sys");
    }

    @Override
    public Taxonomy getTaxonomy(String comment) {
        String category = "insurance";
        String product = "life";

        TaxonomyCategory taxonomyCategory = taxonomyCategoryRepository.findByCategory(category.toLowerCase());
        TaxonomyProduct taxonomyProduct = taxonomyProductRepository.findByProduct(product.toLowerCase());
        return new Taxonomy(model, taxonomyCategory, taxonomyProduct);
    }
}
