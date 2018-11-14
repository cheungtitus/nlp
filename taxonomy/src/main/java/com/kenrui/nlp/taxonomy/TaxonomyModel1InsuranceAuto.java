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

public class TaxonomyModel1InsuranceAuto implements TaxonomyModel {
    private ModelRepository modelRepository;
    private TaxonomyCategoryRepository taxonomyCategoryRepository;
    private TaxonomyProductRepository taxonomyProductRepository;

    private String script;
    private Model model;
    private PythonInterpreter interp;

    public TaxonomyModel1InsuranceAuto(String script, Model model,
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
        interp.exec("print"); // Add empty line for clarity
        interp.exec("print 'sys.prefix=', sys.prefix");
        interp.exec("print 'sys.argv=', sys.argv");
        interp.exec("print 'sys.path=', sys.path");
        interp.exec("print"); // Another blank for clarity

        // Use sys.arv.append to add arguments to be used by python script
        interp.execfile(script);

        String category = "insurance";
        String product = "auto";

        TaxonomyCategory taxonomyCategory = taxonomyCategoryRepository.findByCategory(category.toLowerCase());
        TaxonomyProduct taxonomyProduct = taxonomyProductRepository.findByProduct(product.toLowerCase());
        return new Taxonomy(model, taxonomyCategory, taxonomyProduct);
    }
}
