module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    copy: {
      main: {
        files: [
          {
            expand: true,
            cwd: 'client',
            src: ['*'],
            dest: 'public/',
            filter: 'isFile'
          },
        ]
      },
      partials: {
        files: [
          {
            expand: true,
            cwd: 'client/modules',
            src: ['**/*.html'],
            dest: 'public/'
          }      
        ]
      }
    },
    concat: {
      options: {
        separator: ';'
      },
      lib: {
        src: [
          'node_modules/angular/angular.min.js',
          'node_modules/angular-route/angular-route.min.js'
        ],
        dest: 'public/lib.js'
      },
      modules: {
        src: [
          'client/modules/example/example.config.js',
          'client/modules/example/services/example.service.js',
          'client/modules/example/controllers/example.route.ctrl.js',
          'client/modules/example/controllers/examples-list.directive.ctrl.js',
          'client/modules/example/directives/examples-list.directive.js'
        ],
        dest: 'public/modules.js'
      }
    },
    watch: {
      scripts: {
        files: ['client/**/*'],
        tasks: ['copy', 'concat'],
        options: {
          spawn: false,
        },
      },
    }
  });

  // Load the plugin that provides the "copy" task.
  grunt.loadNpmTasks('grunt-contrib-copy');
  
  // Load the plugin that provides the "concat" task.
  grunt.loadNpmTasks('grunt-contrib-concat');
  
  // Load the plugin that provides the "watch" task.
  grunt.loadNpmTasks('grunt-contrib-watch');

  // Default task(s).
  grunt.registerTask('default', ['copy', 'concat', 'watch']);

};
