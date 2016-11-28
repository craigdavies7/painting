module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        // Copy web assets from bower_components to more convenient directories.
        copy: {
            main: {
                files: [
                    // page specific javascript (dev)
                    {
                        expand: true,
                        flatten: true,
                        src: [
                            'scripts/pages/*'
                        ],
                        dest: 'public/javascripts/pages/'
                    },

                    // Fonts.
                    {
                        expand: true,
                        filter: 'isFile',
                        flatten: true,
                        cwd: 'bower_components/',
                        src: ['bootstrap-sass/assets/fonts/**'],
                        dest: 'public/fonts/'
                    }
                ]
            },
        },

        // Compile SASS files into minified CSS.       
        sass: {
            options: {
                includePaths: ['bower_components/bootstrap-sass/assets/stylesheets']
            },
            development: {
                options: {
                    style: 'expanded',
                    sourcemap: 'auto'
                },
                files: {
                    'public/stylesheets/main.css': 'scss/main.scss'
                }
            },
            dist: {
                options: {
                    outputStyle: 'compressed'
                },
                files: {
                    'public/stylesheets/main.min.css': 'scss/main.scss'
                }
            }
        },
        
        concat: {
          options: {
            separator: ';\n',
          },
          production: {
            src: [
                'bower_components/jquery/dist/jquery.js',
                'bower_components/bootstrap-sass/assets/javascripts/bootstrap.js',
                'scripts/main.js'
            ],
            dest: 'public/javascripts/main.js'
          }
        },

        uglify: {
            production: {
            options: {
                mangle: true,
                beautify: false,
                compress: true
            },
            files: [
                {'public/javascripts/main.min.js': 'public/javascripts/main.js'},
                // minify all page specific javascript
                {
                    expand: true,
                    cwd: 'public/javascripts/pages/',
                    src: ['*.js', '!*.min.js'],
                    dest: 'public/javascripts/pages/',
                    ext: '.min.js'
                }
            ]
            }
        }
    });

    // Load externally defined tasks. 
    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');

    // Establish tasks we can run from the terminal.
    grunt.registerTask('default', ['sass', 'copy', 'concat', 'uglify']);
}