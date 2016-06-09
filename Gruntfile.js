module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    copy: {
      main: {
        files: [
          {
            expand: true,
            cwd: 'client/',
            src: ['**'], dest: 'public/'
          },
        ]
      }
    },
    concat: {
      options: {
        separator: ';'
      },
      lib: {
        src: [
          'node_modules/angular/angular.min.js'
        ],
        dest: 'public/lib.js'
      }
    }
  });

  // Load the plugin that provides the "copy" task.
  grunt.loadNpmTasks('grunt-contrib-copy');
  
  // Load the plugin that provides the "concat" task.
  grunt.loadNpmTasks('grunt-contrib-concat');

  // Default task(s).
  grunt.registerTask('default', ['copy', 'concat']);

};
